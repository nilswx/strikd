package strikd.communication.incoming;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Page;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookClaimLikeHandler extends MessageHandler
{
	public static final String pageId = "";
	
	private static final Logger logger = Logger.getLogger(FacebookClaimLikeHandler.class);
	
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
		if(player.isFacebookLinked() && !player.liked)
		{
			// THANKS!
			player.liked = checkUserLikesPage(player.fbIdentity, session.getServer().getFacebook().getPageId());
			if(player.liked)
			{
				// TODO: give item
				session.send(new AlertMessage("Thanks for liking Strik, here's your crappy item!"));
				
				// Save data
				player.liked = true;
				session.saveData();
			}
		}
		
		// Refresh status
		session.send(new FacebookStatusMessage(player.isFacebookLinked(), player.liked));
	}
	
	private static boolean checkUserLikesPage(FacebookIdentity identity, String pageId)
	{
		try
		{
			// Get list of liked pages
			List<Page> likedPages = identity.getAPI().likeOperations().getPagesLiked();
			for(Page page : likedPages)
			{
				// User likes Strik?
				if(page.getId().equals(pageId))
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{
			logger.warn(String.format("could not verify like status for %s", identity.userId), e);
		}
		
		// Not among the liked pages
		return false;
	}
}
