package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CredentialsMessage extends OutgoingMessage
{
	public CredentialsMessage(int playerId, String token)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeInt(playerId);
		super.writeStr(token);
	}
}
