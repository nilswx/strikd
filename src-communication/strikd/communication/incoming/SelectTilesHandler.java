package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.words.Word;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.WordFoundMessage;
import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
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
			MatchPlayer player = session.getMatchPlayer();
			Match match = player.getMatch();
			Board board = match.getBoard();
			
			// Select all specified tiles
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
					player.selectTile(board.getSquare(x, y));
				}
			}
			
			// Was this the last batch?
			boolean checkWord = request.readBool();
			if(checkWord)
			{
				// Validate selection and concat letters
				StringBuilder letters = new StringBuilder(player.getSelection().size());
				Square previous = null;
				for(Square square : player.getSelection())
				{
					// Still a letter tile here?
					if(square.isTile())
					{
						// Check distance (anti-cheat)
						if(previous == null || (Math.abs(square.x - previous.x) <= 1 && Math.abs(square.y - previous.y) <= 1))
						{
							letters.append(square.getLetter());
							previous = square;
						}
						else
						{
							// Invalid selection
							letters = null;
							break;
						}
					}
				}
				
				// Invalid selection?
				if(letters == null || letters.length() == 0)
				{
					player.clearSelection();
				}
				else
				{
					// Word formed?
					Word word = Word.parse(letters.toString());
					if(match.getLocale().getDictionary(DictionaryType.COMPLETE).contains(word))
					{
						// Assign points!
						int points = word.length();
						session.getMatchPlayer().modScore(+points);
						match.broadcast(new WordFoundMessage(session.getMatchPlayer(), word, +points));
						
						// Clear the 'used' tiles, fill gaps with new letters
						for(Square tile : player.getSelection())
						{
							tile.clear();
						}
						board.fill();
					}
					
					// Clear selection and broadcast any updated tiles
					player.clearSelection();
					match.broadcast(board.getUpdateGenerator().generateUpdates());
				}
			}
		}
	}
}
