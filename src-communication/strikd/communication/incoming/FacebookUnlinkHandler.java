package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class FacebookUnlinkHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "UNLINK";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		if(session.getPlayer().fbIdentity != null)
		{
			// Delete link and reset player name to random guest name
		}
	}
}
