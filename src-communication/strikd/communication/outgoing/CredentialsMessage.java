package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CredentialsMessage extends OutgoingMessage
{
	public CredentialsMessage(long playerId, String token)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeLong(playerId);
		super.writeStr(token);
	}
}
