package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.PlayerInfoMessage;
import strikd.communication.outgoing.PlayerUnknownMessage;
import strikd.game.items.ItemTypesMessageCache;
import strikd.game.player.Experience;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class LoginHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.LOGIN;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Not logged in already?
		if(session.isLoggedIn()) return;
			
		// Read login info
		int playerId = request.readInt();
		String token = request.readStr();
		String hardware = request.readStr();
		String systemVersion = request.readStr();
		
		// Valid credentials?
		Player player = session.getServer().getPlayerRegister().findPlayer(playerId);
		if(player == null || !token.equals(player.getToken()))
		{
			// Don't know what the client is doing, but his credentials are invalid
			session.send(new PlayerUnknownMessage(playerId));
			session.end(String.format("bad login on player #%d", playerId));
		}
		else
		{
			// Login OK!
			session.setPlayer(player, String.format("%s @ %s", hardware, systemVersion));
			
			// Push player data
			session.send(new PlayerInfoMessage(player));
			session.send(new CurrencyBalanceMessage(player.getBalance()));
			
			// Push levels ranges
			session.sendCopy(Experience.getLevelsMessage());
			
			// Welcome!
			session.sendAlert("Welcome aboard Strik! Server: %s\r\rLogins: %d\rPlatform: %s\rMotto: \"%s\"\rBalance: %d coins\r\rThanks for flying with us!",
					session.getServer().getServerCluster().getSelf(),
					player.getLogins(),
					player.getPlatform(),
					player.getMotto(),
					player.getBalance());
			
			// Will force client to validate
			session.send(new FacebookStatusMessage(player.isFacebookLinked(), player.isLiked()));
			
			// Push item type registry
			session.sendCopy(ItemTypesMessageCache.getMessage());
		}
	}
}
