package strikd.communication.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.types.User;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CredentialsMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.facebook.FacebookIdentity;
import strikd.game.facebook.FacebookInviteManager;
import strikd.game.player.Player;
import strikd.game.player.PlayerRegister;
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
		
		// Test token by retrieving own user object
		User user = null;
		try
		{
			user = newIdentity.getAPI().fetchObject("me", User.class);
			newIdentity.setUserId(Long.parseLong(user.getId()));
		}
		catch(Exception ex)
		{
			logger.warn("Facebook link for {} failed!", session.getPlayer(), ex);
			newIdentity = null;
		}
		
		// Valid token?
		if(newIdentity != null)
		{
			// Awesome! We now have a user ID. Who's account is this?
			PlayerRegister register = session.getServer().getPlayerRegister();
			Player oldPlayer = register.getDatabase().find(Player.class).where().eq("fb_uid", newIdentity.getUserId()).findUnique();
			
			// Player already exists? (and is different)
			if(oldPlayer != null && oldPlayer.getId() != session.getPlayer().getId())
			{
				// Reset token and delete the current account
				oldPlayer.setToken(register.generateToken());
				register.savePlayer(oldPlayer);
				register.deletePlayer(session.getPlayer());
				
				// Tell client to use this account!
				session.send(new CredentialsMessage(oldPlayer.getId(), oldPlayer.getToken()));
				
				// Ditch this session
				session.setSaveOnLogout(false);
				session.end(String.format("recovered %s", oldPlayer));
				
				// Bail here
				return;
			}
			else
			{
				// Rename this player to person's first name
				session.renamePlayer(user.getFirstName());
				
				// Change the avatar
				session.changeAvatar("f" + newIdentity.getUserId());
					
				// Process pending invites
				FacebookInviteManager inviteMgr = session.getServer().getFacebook().getInviteMgr();
				inviteMgr.processInvites(newIdentity.getUserId());
		
				// Save current link status
				session.getPlayer().setFacebook(newIdentity);
				session.saveData();
			}
		}
		
		// Send current status
		session.send(new FacebookStatusMessage(session.getPlayer()));
	}
}
