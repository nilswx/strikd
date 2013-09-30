package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class VersionCheckMessage extends OutgoingMessage
{
	public VersionCheckMessage(int major, int minor, String tag)
	{
		super(Opcodes.Outgoing.VERSIONCHECK);
		super.writeByte((byte)major);
		super.writeByte((byte)minor);
		super.writeStr(tag);
	}
}
