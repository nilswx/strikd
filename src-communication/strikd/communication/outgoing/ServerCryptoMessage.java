package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ServerCryptoMessage extends OutgoingMessage
{
	public ServerCryptoMessage(byte[] key)
	{
		super(Opcodes.Outgoing.SERVER_CRYPTO);
		super.writeStr(new String(key));
	}
}
