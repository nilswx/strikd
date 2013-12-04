package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class LevelsMessage extends OutgoingMessage
{
	public LevelsMessage(int[] levels)
	{
		super(Opcodes.Outgoing.LEVELS);
		super.writeByte((byte)levels.length);
		for(byte level = 1; level <= levels.length; level++)
		{
			super.writeByte(level);
			super.writeInt(levels[level - 1]); // begin XP
		}
	}
}
