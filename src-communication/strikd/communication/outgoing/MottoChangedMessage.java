package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class MottoChangedMessage extends OutgoingMessage
{
	public MottoChangedMessage(String newMotto)
	{
		super(Opcodes.Outgoing.AVATAR_CHANGED);
		super.writeStr(newMotto);
	}
}
