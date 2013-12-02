package strikd.communication.incoming;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookRecoverResultMessage;
import strikd.game.player.Player;
import strikd.game.player.PlayerRegister;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;

public class FacebookRecoverPlayerHandler extends MessageHandler
{
	private static final Logger logger = LoggerFactory.getLogger(FacebookRecoverPlayerHandler.class);
	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_RECOVER_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Not logged iny?
		if(!session.isLoggedIn())
		{
			// Process the token
			FacebookClient facebook = new DefaultFacebookClient(request.readStr());
			
			// Find the player that is linked to this token
			Player player;
			try
			{
				// This request will only succeed when the token is valid
				List<JsonObject> res = facebook.executeFqlQuery("SELECT uid FROM user WHERE uid=me()", JsonObject.class);
				long userId = res.get(0).getLong("uid");
				
				// Awesome! We now have a user ID. Who's account is this?
				PlayerRegister register = session.getServer().getPlayerRegister();
				player = register.getDatabase().find(Player.class).where().eq("fb_uid", userId).findUnique();
			}
			catch(Exception ex)
			{
				player = null;
			}
			
			// Logging purposes
			if(player != null)
			{
				logger.info("{} recovered player {}", session.getConnection().getIpAddress(), player);
			}
			else
			{
				logger.warn("{} failed to recover player, token invalid or not linked to player", session.getConnection().getIpAddress());
			}
			
			// Send the result!
			session.send(new FacebookRecoverResultMessage(player));
		}
	}
}
