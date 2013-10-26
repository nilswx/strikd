package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class LevelUpMessage extends OutgoingMessage
{
	public LevelUpMessage(int levels)
	{
		super(Opcodes.Outgoing.CURRENCY_BALANCE);
		super.writeByte((byte)levels);
	}
	
	public void addLevel(int level, int beginXP, int currentXP, int endXP)
	{
		super.writeByte((byte)level);
		super.writeInt(beginXP);
		super.writeInt(currentXP);
		super.writeInt(endXP);
	}
}
