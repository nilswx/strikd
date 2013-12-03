package strikd.sessions;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import strikd.Server;
import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.communication.incoming.MessageHandlers;
import strikd.communication.outgoing.NameChangedMessage;
import strikd.communication.outgoing.ServerCryptoMessage;
import strikd.communication.outgoing.SessionInfoMessage;
import strikd.communication.outgoing.VersionCheckMessage;
import strikd.game.items.ItemInventory;
import strikd.game.items.ItemTypeRegistry;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.player.Experience;
import strikd.game.player.Player;
import strikd.game.stream.activity.ItemReceivedStreamItem;
import strikd.game.stream.activity.LevelUpStreamItem;
import strikd.net.NetConnection;
import strikd.net.codec.IncomingMessage;
import strikd.net.codec.OutgoingMessage;
import strikd.util.RandomUtil;

public class Session extends Server.Referent
{
	private static final boolean USE_CRYPTO = true;

	private static final Logger logger = LoggerFactory.getLogger(Session.class);

	private final long sessionId;
	private final NetConnection connection;
	private boolean isEnded;

	private boolean handshakeOK;
	private Player player;
	private boolean saveOnLogout;
	
	private MatchPlayer matchPlayer;
	private PlayerQueue.Entry queueEntry;
	
	private List<Integer> friendList;
	private List<Integer> following;
	
	private ItemInventory inventory;

	public Session(long sessionId, NetConnection connection, Server server)
	{
		super(server);
		this.sessionId = sessionId;
		this.connection = connection;
	}

	public void hello()
	{
		// Forces client to validate version and update if needed
		this.send(new VersionCheckMessage(1, 0, "Waterduck"));

		// Crypto enabled?
		if(USE_CRYPTO)
		{
			// Initiate handshake flow
			byte[] key = UUID.randomUUID().toString().getBytes();
			this.send(new ServerCryptoMessage(key));
			this.connection.setServerCrypto(key);
		}
		else
		{
			// Short-circuit flow
			this.handshakeOK();
		}
	}

	public void handshakeOK()
	{
		// Handshake OK!
		logger.debug("handshake OK");
		this.handshakeOK = true;

		// Send session info
		ServerDescriptor server = this.getServer().getServerCluster().getSelf();
		this.send(new SessionInfoMessage(this.sessionId, server.getName()));
	}

	public void end(String reason)
	{
		this.getServer().getSessionMgr().endSession(this.sessionId, reason);
	}

	public void onEnd()
	{
		if(!this.isEnded)
		{
			this.isEnded = true;
			this.connection.close();
			if(this.isLoggedIn())
			{
				this.onLogout();
			}
		}
	}

	public void onNetClose(String reason)
	{
		this.end(reason);
	}

	private void onLogin()
	{
		// Save on logout!
		this.setSaveOnLogout(true);
		
		// Parse inventory
		this.inventory = new ItemInventory();
		
		// Post a levelup activity
		if(this.player.getLogins() > 1)
		{
			LevelUpStreamItem lup = new LevelUpStreamItem();
			lup.setPlayer(this.player);
			lup.setLevel(RandomUtil.nextInt(Experience.MAX_LEVEL));
			this.getServer().getActivityStream().postItem(lup);
			
			// Post an item received activity
			ItemReceivedStreamItem ir = new ItemReceivedStreamItem();
			ir.setPlayer(this.player);
			ir.setItem(RandomUtil.pickOne(ItemTypeRegistry.allTypes()));
			this.getServer().getActivityStream().postItem(ir);
		}
		
		// Subscribe to own stream items
		this.getFollowing().add(this.player.getId());
	}

	private void onLogout()
	{
		// Exit queue or match
		if(this.isInQueue())
		{
			this.exitQueue();
		}
		else if(this.isInMatch())
		{
			this.exitMatch();
		}

		// Save player?
		if(this.saveOnLogout)
		{
			this.saveData();
		}
	}

	public void onNetMessage(IncomingMessage msg)
	{
		logger.debug("< {}", msg);

		if(this.handshakeOK || (msg.op == Opcodes.Incoming.CLIENT_CRYPTO))
		{
			MessageHandlers.get(msg.op).handle(this, msg);
		}
		else
		{
			this.end(String.format("received %s before handshake", msg.op));
		}
	}

	public void send(OutgoingMessage msg)
	{
		logger.debug("> {}", msg);

		this.connection.send(msg);
	}
	
	public long getSessionId()
	{
		return this.sessionId;
	}

	public NetConnection getConnection()
	{
		return this.connection;
	}

	public boolean isHandshakeOK()
	{
		return this.handshakeOK;
	}

	public boolean isLoggedIn()
	{
		return (this.player != null);
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public void setPlayer(Player player, String platform)
	{
		if(this.player == null)
		{
			this.player = player;
			this.player.setPlatform(platform);
			this.getServer().getSessionMgr().completeLogin(this);
			this.onLogin();
		}
	}

	public void saveData()
	{
		if(this.isLoggedIn())
		{
			this.getServer().getPlayerRegister().savePlayer(this.player);
		}
	}
	
	public void setSaveOnLogout(boolean saveOnLogout)
	{
		this.saveOnLogout = saveOnLogout;
	}

	public void renamePlayer(String newName)
	{
		if(this.isLoggedIn())
		{
			this.player.setName(newName);
			this.send(new NameChangedMessage(newName));
		}
	}

	public boolean isInQueue()
	{
		return (this.queueEntry != null);
	}

	public boolean isInMatch()
	{
		return (this.matchPlayer != null);
	}

	public MatchPlayer getMatchPlayer()
	{
		return this.matchPlayer;
	}

	public void setMatchPlayer(MatchPlayer player)
	{
		// Leave the queue!
		if(this.queueEntry != null)
		{
			this.exitQueue();
		}

		// Leave the old match
		if(this.matchPlayer != null)
		{
			this.matchPlayer.leave();
		}

		// Set the new player reference
		this.matchPlayer = player;
	}

	public Match getMatch()
	{
		return this.matchPlayer.getMatch();
	}

	public void exitMatch()
	{
		this.setMatchPlayer(null);
	}

	public void exitQueue()
	{
		this.setQueueEntry(null);
	}

	public void setQueueEntry(PlayerQueue.Entry entry)
	{
		if(this.queueEntry != null)
		{
			this.queueEntry.exit();
		}
		this.queueEntry = entry;
	}
	
	public void setFriendList(List<Integer> friendList)
	{
		this.friendList = friendList;
	}
	
	public List<Integer> getFriendList()
	{
		return this.friendList;
	}

	public List<Integer> getFollowing()
	{
		if(this.following == null)
		{
			this.following = Lists.newArrayList();
		}
		return this.following;
	}
	
	public ItemInventory getInventory()
	{
		return this.inventory;
	}

	public void setInventory(ItemInventory inventory)
	{
		this.inventory = inventory;
	}
}
