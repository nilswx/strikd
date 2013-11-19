package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CredentialsMessage extends OutgoingMessage
{
	public CredentialsMessage(long id, String token)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeStr(id);
		super.writeStr(token);
	}
}
