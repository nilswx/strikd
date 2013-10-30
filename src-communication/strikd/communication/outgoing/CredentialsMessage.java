package strikd.communication.outgoing;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CredentialsMessage extends OutgoingMessage
{
	public CredentialsMessage(ObjectId playerId, String token)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeStr(playerId.toString());
		super.writeStr(token);
	}
}
