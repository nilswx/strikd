package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class ChangeLocaleHandler extends MessageHandler
{	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CHANGE_LOCALE;
	}
	
	@Override
	public final void handle(Session session, IncomingMessage request)
	{
		String newLocale = request.readStr();
		if(session.getServer().getLocaleMgr().getBundle(newLocale) != null)
		{
			session.getPlayer().setLocale(newLocale);
		}
	}
}
