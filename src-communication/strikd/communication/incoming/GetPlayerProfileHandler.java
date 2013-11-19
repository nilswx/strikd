package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.PlayerProfileMessage;
import strikd.game.player.PlayerProfile;
import strikd.game.player.PlayerRegister;
import strikd.net.codec.IncomingMessage;

public class GetPlayerProfileHandler extends MessageHandler
{	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_PLAYER_PROFILE;
	}
	
	@Override
	public final void handle(Session session, IncomingMessage request)
	{
		// Lookup profile
		PlayerRegister register = session.getServer().getPlayerRegister();
		PlayerProfile profile = register.getProfile(request.readLong());
		
		// Profile exists?
		if(profile != null)
		{
			session.send(new PlayerProfileMessage(profile));
		}
	}
}
