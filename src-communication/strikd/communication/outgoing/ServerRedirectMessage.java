package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ServerRedirectMessage extends OutgoingMessage
{
	public ServerRedirectMessage(String newHost)
	{
		super(Opcodes.Outgoing.SERVER_REDIRECT);
		super.writeStr(newHost);
	}
}
