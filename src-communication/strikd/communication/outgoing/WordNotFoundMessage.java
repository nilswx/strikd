package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class WordNotFoundMessage extends OutgoingMessage
{
	public WordNotFoundMessage()
	{
		super(Opcodes.Outgoing.WORD_NOT_FOUND);
	}
}
