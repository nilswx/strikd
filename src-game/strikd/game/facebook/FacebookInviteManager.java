package strikd.game.facebook;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import strikd.Server;
import strikd.game.player.Player;
import strikd.game.player.PlayerRegister;

public class FacebookInviteManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(FacebookInviteManager.class);
	
	public FacebookInviteManager(Server server)
	{
		super(server);
		//this.dbInvites = server.getDbCluster().getCollection("invites");
		
		logger.info(String.format("pending invites for %d persons", 0));//this.dbInvites.count()));
	}
	
	public void registerInvite(long personId, Player inviter)
	{
		//this.dbInvites.update("{_id:#}", personId).upsert().with("{$addToSet:{by:#}}", inviter.getId());	
		logger.info(String.format("%s invited person #%d!", inviter, personId));
	}
	
	public void processInvites(long personId)
	{
		// Was this FB user invited by existing players?
		List<Long> invites = Lists.newArrayList();//this.dbInvites.findAndModify("{_id:#}", personId).remove().as(InvitedByList.class);
		if(invites != null)
		{
			// Reward these guys
			PlayerRegister register = this.getServer().getPlayerRegister();
			for(long playerId : invites)
			{
				// Player _should_ still exist
				Player player = register.findPlayer(playerId);
				if(player != null)
				{
					logger.info(String.format("FB user #%d linked, rewarding %s for invite", personId, player));
				}
			}
		}
	}
}
