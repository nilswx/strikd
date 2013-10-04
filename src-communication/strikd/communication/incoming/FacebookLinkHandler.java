package strikd.communication.incoming;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.game.user.FacebookIdentity;
import strikd.net.codec.IncomingMessage;

public class FacebookLinkHandler extends MessageHandler
{
	private static final Logger logger = Logger.getLogger(FacebookLinkHandler.class);
	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_LINK;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Receive latest Facebook link credentials
		FacebookIdentity newIdentity = new FacebookIdentity();
		newIdentity.token = request.readStr();
		
		// Validate token (perform on a background worker system?)
		Facebook facebook = newIdentity.getAPI();
		try
		{
			// If this operation succeeds, then the token is valid
			FacebookProfile profile = facebook.userOperations().getUserProfile();
			
			// Set user ID for quick lookups later
			newIdentity.userId = Long.parseLong(profile.getId());
				
			// Rename user to user's first name
			session.renameUser(profile.getFirstName());
		}
		catch(Exception e)
		{
			logger.warn(String.format("Facebook link failed!", e));
			newIdentity = null;
		}
		
		// Save current link status
		session.getUser().fbIdentity = newIdentity;
		session.saveData();
		
		// Send current status
		session.send(new FacebookStatusMessage(session.getUser().isFacebookLinked()));
	}
	
	//logger.debug(String.format("FB #%s (\"%s %s\", %s) has %d FB friends", profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), facebook.friendOperations().getFriendIds().size())); 
}
