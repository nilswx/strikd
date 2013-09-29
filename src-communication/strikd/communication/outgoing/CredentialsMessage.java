package strikd.communication.outgoing;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class CredentialsMessage extends OutgoingMessage
{
	public CredentialsMessage(ObjectId userId, String token)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeStr(userId.toString());
		super.writeStr(token);
	}
}
