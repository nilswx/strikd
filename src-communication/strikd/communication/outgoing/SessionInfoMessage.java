package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class SessionInfoMessage extends OutgoingMessage
{
	public SessionInfoMessage(long sessionId, String serverName)
	{
		super(Opcodes.Outgoing.SESSION_INFO);
		super.writeLong(sessionId);
		super.writeStr(serverName);
	}
}
