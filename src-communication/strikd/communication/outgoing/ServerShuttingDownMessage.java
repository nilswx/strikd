package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ServerShuttingDownMessage extends OutgoingMessage
{
	public ServerShuttingDownMessage(String info)
	{
		super(Opcodes.Outgoing.SERVER_SHUTTING_DOWN);
		super.writeStr(info);
	}
}
