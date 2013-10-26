package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ExperienceMessage extends OutgoingMessage
{
	public ExperienceMessage(int added, int levels)
	{
		super(Opcodes.Outgoing.CURRENCY_BALANCE);
		super.writeInt(added);
		super.writeByte((byte)levels);
	}
	
	public void writeLevel(int level, int beginXP, int currentXP, int endXP)
	{
		super.writeByte((byte)level);
		super.writeInt(beginXP);
		super.writeInt(currentXP);
		super.writeInt(endXP);
	}
}
