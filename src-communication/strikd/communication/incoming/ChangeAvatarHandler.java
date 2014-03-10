package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.game.player.Avatars;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class ChangeAvatarHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CHANGE_AVATAR;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Fetch player & desired avatar
		Player player = session.getPlayer();
		String requestedAvatar = request.readStr();
		
		// Determine new avatar
		String newAvatar = player.getAvatar();
		if(requestedAvatar.equals("f") && player.isFacebookLinked())
		{
			newAvatar = ("f" + player.getFacebook().getUserId());
		}
		else
		{
			try
			{
				// Attempt to parse the ID
				int avatarId = Integer.parseInt(requestedAvatar);
				if(avatarId >= 1 && avatarId <= Avatars.AMOUNT_OF_AVATARS)
				{
					newAvatar = Integer.toString(avatarId);
				}
			}
			catch(NumberFormatException e)
			{
				// Oh noes!
			}
		}
		
		// Issue the change
		session.changeAvatar(newAvatar);
	}
}
