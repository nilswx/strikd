package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class ChangeNameHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "RENAME";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		String newName = request.get("name");
	}
}
