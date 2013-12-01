package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.game.util.InputFilter;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class ChangeMottoHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CHANGE_MOTTO;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// What motto do you want?
		String requestedMotto = request.readStr();
		
		// Filter & validate new motto
		String newMotto = InputFilter.sanitizeInput(requestedMotto);
		
		// Cool!
		session.getPlayer().setMotto(newMotto);
	}
}
