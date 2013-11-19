package strikd.communication.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.facebook.api.FacebookProfile;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.facebook.FacebookInviteManager;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class FacebookLinkHandler extends MessageHandler
{
	private static final Logger logger = LoggerFactory.getLogger(FacebookLinkHandler.class);
	
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
		newIdentity.setToken(request.readStr());
		
		try
		{
			// If this operation succeeds, then the token is valid
			FacebookProfile profile = newIdentity.getAPI().userOperations().getUserProfile();
			
			// Set user ID for quick lookups later
			newIdentity.setUserId(Long.parseLong(profile.getId()));
			
			// Rename player to person's first name
			newIdentity.setName(profile.getName());
			session.renamePlayer(profile.getFirstName());
			
			// TODO: Set country
			Object loc = profile.getLocation();
			//session.getPlayer().country = profile.getL
			
			// Process pending invites
			FacebookInviteManager inviteMgr = session.getServer().getFacebook().getInviteMgr();
			inviteMgr.processInvites(newIdentity.getUserId());
		}
		catch(Exception e)
		{
			logger.warn(String.format("Facebook link for %s failed!", session.getPlayer()), e);
			newIdentity = null;
		}
		
		// Save current link status
		session.getPlayer().setFacebook(newIdentity);
		session.saveData();
		
		// Send current status
		session.send(new FacebookStatusMessage(session.getPlayer().isFacebookLinked(), session.getPlayer().isLiked()));
	}
	
	//logger.debug(String.format("FB #%s (\"%s %s\", %s) has %d FB friends", profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), facebook.friendOperations().getFriendIds().size())); 
}
