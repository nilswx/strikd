package strikd.communication.outgoing;

import strikd.communication.Opcodes.Outgoing;
import strikd.net.codec.OutgoingMessage;

public class AlertMessage extends OutgoingMessage
{
	public AlertMessage(String text)
	{
		super(Outgoing.ALERT);
		super.writeStr(text);
	}
}
