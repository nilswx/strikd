package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Tile;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;
import strikd.words.Word;

public class WordFoundMessage extends OutgoingMessage
{
	public WordFoundMessage(MatchPlayer player, Word word, int points, List<Tile> tiles)
	{
		super(Opcodes.Outgoing.WORD_FOUND);
		super.writeByte((byte)player.getActorId());
		super.writeStr(word.toString());
		super.writeInt(points);
		super.writeByte((byte)tiles.size());
		for(Tile tile : tiles)
		{
			super.writeByte(tile.getTileId());
		}
	}
}
