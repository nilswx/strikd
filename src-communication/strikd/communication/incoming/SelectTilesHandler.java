package strikd.communication.incoming;

import java.util.ArrayList;
import java.util.List;

import strikd.sessions.Session;
import strikd.words.Word;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.WordFoundMessage;
import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.game.match.Match;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.IncomingMessage;

public class SelectTilesHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.SELECT_TILES;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// In valid match, and not frozen, etc?
		if(session.isInMatch())
		{
			// Short-hand variables
			Match match = session.getMatch();
			Board board = match.getBoard();
			
			// Build list of selected tiles and letters while they are being processed
			List<Square> tiles = new ArrayList<Square>();
			StringBuilder letters = new StringBuilder();
			
			// Process all specified tiles
			int amount = request.readByte();
			for(int i = 0; i < amount; i++)
			{
				// Unpack position
				byte pos = request.readByte();
				int x = pos >> 4;
				int y = pos & 0x0F;
						
				// In range?
				if(board.squareExists(x, y))
				{
					// Is actually a tile?
					Square square = board.getSquare(x, y);
					if(square.isTile())
					{
						tiles.add(square);
						letters.append(square.getLetter());
					}
				}
			}
			
			// Word formed?
			Word word = Word.parse(letters.toString());
			if(match.getLocale().getDictionary(DictionaryType.COMPLETE).contains(word))
			{
				// Assign points!
				int points = word.length();
				session.getMatchPlayer().modScore(+points);
				match.broadcast(new WordFoundMessage(session.getMatchPlayer(), word, +points));
				
				// Clear tiles, fill gaps
				for(Square tile : tiles)
				{
					tile.clear();
				}
				board.fill();
			}
			else
			{
				// Reset letters
				for(Square tile : tiles)
				{
					tile.setLetter(tile.getLetter());
				}
			}
			
			// Broadcast updated tiles
			match.broadcast(board.getUpdateGenerator().generateUpdates());
		}
	}
}
