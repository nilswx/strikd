package strikd.communication.incoming;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.json.JsonObject;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookClaimLikeHandler extends MessageHandler
{
	public static final String pageId = "";
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookClaimLikeHandler.class);
	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_CLAIM_LIKE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Allowed to claim reward?
		Player player = session.getPlayer();
		if(player.isFacebookLinked() && !player.isLiked())
		{
			// THANKS!
			player.setLiked(this.checkUserLikesPage(player.getFacebook(), session.getServer().getFacebook().getPageId()));
			if(player.isLiked())
			{
				// TODO: give item
				session.sendAlert("Thanks for liking Strik, here's your crappy item!");
				
				// Save data
				session.saveData();
			}
		}
		
		// Refresh status
		session.send(new FacebookStatusMessage(player));
	}
	
	private boolean checkUserLikesPage(FacebookIdentity identity, String pageId)
	{
		try
		{
			// Test whether this user likes the page
			List<JsonObject> results = identity.getAPI().executeFqlQuery("SELECT 1 FROM page_fan WHERE uid=me() AND page_id=" + pageId, JsonObject.class);
			return (results.size() == 1);
		}
		catch(Exception e)
		{
			logger.warn("could not verify like status for {}", identity, e);
		}
		
		// Not among the liked pages
		return false;
	}
}
