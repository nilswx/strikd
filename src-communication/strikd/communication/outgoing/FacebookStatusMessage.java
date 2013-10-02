package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class FacebookStatusMessage extends OutgoingMessage
{
	public FacebookStatusMessage(boolean isLinked)
	{
		super(Opcodes.Outgoing.FACEBOOK_STATUS);
		super.writeBool(isLinked);
	}
}
