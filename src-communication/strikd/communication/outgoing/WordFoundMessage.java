package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;
import strikd.words.Word;

public class WordFoundMessage extends OutgoingMessage
{
	public WordFoundMessage(MatchPlayer player, Word word, int points)
	{
		super(Opcodes.Outgoing.WORD_FOUND);
		super.writeByte((byte)player.getPlayerId());
		super.writeStr(word.toString());
		super.writeInt(points);
	}
}
