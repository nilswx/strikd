package strikd.communication.incoming;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.FacebookProfile;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.facebook.FacebookInviteManager;
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
			
			// Rename player to person's first name
			session.renamePlayer(profile.getFirstName());
			
			// TODO: Set country
			//session.getPlayer().country = profile.getL
			
			// Process pending invites
			FacebookInviteManager inviteMgr = session.getServer().getFacebook().getInviteMgr();
			inviteMgr.processInvites(newIdentity.userId);
		}
		catch(Exception e)
		{
			logger.warn(String.format("Facebook link for %s failed!", session.getPlayer()), e);
			newIdentity = null;
		}
		
		// Save current link status
		session.getPlayer().fbIdentity = newIdentity;
		session.saveData();
		
		// Send current status
		session.send(new FacebookStatusMessage(session.getPlayer().isFacebookLinked(), session.getPlayer().liked));
	}
	
	//logger.debug(String.format("FB #%s (\"%s %s\", %s) has %d FB friends", profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), facebook.friendOperations().getFriendIds().size())); 
}
