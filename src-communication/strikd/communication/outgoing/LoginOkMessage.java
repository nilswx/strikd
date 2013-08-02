package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class LoginOkMessage extends OutgoingMessage
{
	public LoginOkMessage()
	{
		super(Opcodes.Outgoing.LOGIN_OK);
	}
}
