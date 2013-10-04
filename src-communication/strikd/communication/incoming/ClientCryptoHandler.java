package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class ClientCryptoHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CLIENT_CRYPTO;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Read key bytes
		String keyStr = request.readStr();
		byte[] key = keyStr.getBytes();
		
		// Install decryption
		session.getConnection().setClientCrypto(key);
		
		// Complete handshake
		session.handshakeOK();
	}
}
