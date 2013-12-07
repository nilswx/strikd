package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class HighScoresMessage extends OutgoingMessage
{
	public HighScoresMessage(int[] levels)
	{
		super(Opcodes.Outgoing.LEVELS);
		super.writeByte((byte)levels.length);
		for(int beginXP : levels)
		{
			super.writeInt(beginXP);
		}
	}
}
