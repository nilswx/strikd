package strikd.communication.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.PlayerInfoMessage;
import strikd.communication.outgoing.PlayerUnknownMessage;
import strikd.game.items.ItemTypesMessageCache;
import strikd.game.player.Experience;
import strikd.game.player.Player;
import strikd.game.util.CountryResolver;
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
			// Update country
			String newCountry = CountryResolver.getCountryCode(session.getConnection().getIpAddress());
			if(newCountry != null)
			{
				player.setCountry(newCountry);
			}
			
			// No country? (for localhost testing)
			if(player.getCountry().isEmpty())
			{
				player.setCountry("nl");
				logger.debug("{} had no country, setting it to '{}'", player, player.getCountry());
			}
			
			// Login OK!
			session.setPlayer(player, String.format("%s @ %s", hardware, systemVersion));
			
			// Push player data
			session.send(new PlayerInfoMessage(player));
			session.send(new CurrencyBalanceMessage(player.getBalance()));
			
			// Add experience!
			Experience.addExperience(player, session, +10);
			
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
			session.getConnection().sendCopy(ItemTypesMessageCache.getMessage());
		}
	}
}
