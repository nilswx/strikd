package strikd.communication.incoming;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.game.user.FacebookIdentity;
import strikd.net.codec.IncomingMessage;

public class FacebookLinkHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_LINK;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Receive latest Facebook link credentials
		FacebookIdentity identity = new FacebookIdentity();
		identity.userId = request.readLong();
		identity.token = request.readStr();
		
		// Validate identity
		Facebook facebook = identity.getAPI();
		if(facebook.isAuthorized())
		{
			// Set identity
			session.getUser().fbIdentity = identity;
			
			// Rename user to user's first name
			FacebookProfile profile = facebook.userOperations().getUserProfile();
			session.renameUser(profile.getFirstName());
		}
		else
		{
			session.getUser().fbIdentity = null;
		}
		session.saveData();
		
		// Send current status
		session.send(new FacebookStatusMessage(true));
	}
	
	//logger.debug(String.format("FB #%s (\"%s %s\", %s) has %d FB friends", profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), facebook.friendOperations().getFriendIds().size())); 
}
