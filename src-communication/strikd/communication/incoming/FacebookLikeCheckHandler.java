package strikd.communication.incoming;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Page;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.game.user.User;
import strikd.net.codec.IncomingMessage;

public class FacebookLikeCheckHandler extends MessageHandler
{
	public static final String pageId = "";
	
	private static final Logger logger = Logger.getLogger(FacebookLikeCheckHandler.class);
	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_LIKE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Allowed to claim reward?
		User user = session.getUser();
		if(user.isFacebookLinked() && !user.liked)
		{
			// THANKS!
			user.liked = checkUserLikesPage(user);
			if(user.liked)
			{
				// TODO: give item
				session.send(new AlertMessage("Thanks for liking Strik, here's your crappy item!"));
				
				// Save data
				user.liked = true;
				session.saveData();
			}
		}
		
		// Refresh status
		session.send(new FacebookStatusMessage(user.isFacebookLinked(), user.liked));
	}
	
	private static boolean checkUserLikesPage(User user)
	{
		try
		{
			// Get list of liked pages
			List<Page> likedPages = user.fbIdentity.getAPI().likeOperations().getPagesLiked();
			for(Page page : likedPages)
			{
				// User likes Strik?
				if(page.getId().equals(FacebookLikeCheckHandler.pageId))
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{
			logger.warn(String.format("could not verify like status for %s", user), e);
		}
		
		// Not among the liked pages
		return false;
	}
}
