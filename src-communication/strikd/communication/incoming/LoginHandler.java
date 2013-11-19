package strikd.communication.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.CredentialsMessage;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.ItemsMessage;
import strikd.communication.outgoing.PlayerInfoMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class LoginHandler extends MessageHandler
{
	private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.LOGIN;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Not logged in already?
		if(!session.isLoggedIn())
		{
			// Read login info
			long playerId = Long.parseLong(request.readStr());
			String token = request.readStr();
			String hardware = request.readStr();
			String systemVersion = request.readStr();
			
			// Correct account and password?
			Player player = session.getServer().getPlayerRegister().findPlayer(playerId);
			if(player == null || !token.equals(player.getToken()))
			{
				// Hmm...
				logger.warn("incorrect login for player #{}, creating new player", playerId);
				
				// Create a new player and treat it like a registration
				player = session.getServer().getPlayerRegister().newPlayer();
				session.send(new CredentialsMessage(player.getId(), player.getToken()));
			}
			else
			{
				// Login OK!
				session.setPlayer(player, String.format("%s @ %s", hardware, systemVersion));
				
				// Push player data
				session.send(new PlayerInfoMessage(player));
				session.send(new FacebookStatusMessage(player.isFacebookLinked(), player.isLiked()));
				session.send(new CurrencyBalanceMessage(player.getBalance()));
				session.send(new ItemsMessage(player.getItems()));
				
				// Welcome!
				session.send(new AlertMessage(String.format("Welcome aboard Strik (server %s), bier en tieten ad infinitum!\r\rLogins: %d\rPlatform: %s\r\rThanks for staying with us!", session.getServer().getServerCluster().getSelf(), player.getLogins(), player.getPlatform())));
			}
		}
	}
}
