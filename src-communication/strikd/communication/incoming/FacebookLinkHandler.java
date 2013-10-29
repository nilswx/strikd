package strikd.communication.incoming;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.FacebookProfile;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.facebook.InviteManager;
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
		// Constructor new identity with latest token
		FacebookIdentity newIdentity = new FacebookIdentity();
		newIdentity.token = request.readStr();
		
		try
		{
			// If this operation succeeds, then the token is valid
			FacebookProfile profile = newIdentity.getAPI().userOperations().getUserProfile();
			
			// Set user ID for quick lookups later
			newIdentity.userId = Long.parseLong(profile.getId());
			
			// Rename user to user's first name
			session.renameUser(profile.getFirstName());
			
			// TODO: Set country
			//session.getUser().country = profile.getL
			
			// Process pending invites
			InviteManager inviteMgr = session.getServer().getFacebook().getInviteMgr();
			inviteMgr.processInvites(newIdentity.userId);
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
		session.send(new FacebookStatusMessage(session.getUser().isFacebookLinked(), session.getUser().liked));
	}
	
	//logger.debug(String.format("FB #%s (\"%s %s\", %s) has %d FB friends", profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), facebook.friendOperations().getFriendIds().size())); 
}
