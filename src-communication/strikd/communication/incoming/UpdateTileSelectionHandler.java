package strikd.communication.incoming;

import java.util.ArrayList;
import java.util.List;

import strikd.communication.outgoing.BoardUpdateMessage;
import strikd.sessions.Session;
import strikd.words.Word;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.TileSelectionClearedMessage;
import strikd.communication.outgoing.TileSelectionExtendedMessage;
import strikd.communication.outgoing.WordFoundMessage;
import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.IncomingMessage;

public class UpdateTileSelectionHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.UPDATE_TILE_SELECTION;
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
			List<Square> newSelected = new ArrayList<Square>();
			for(int i = 0; i < amount; i++)
			{
				// Unpack position
				byte pos = request.readByte();
				int x = pos >> 4;
				int y = pos & 0x0F;

                // Valid addition?
                Square square = board.getSquare(x, y);
				if(square != null)
				{
                    player.selectTile(board.getSquare(x, y));
                    newSelected.add(square);
				}
			}
			
			// Was this the last batch?
			boolean isComplete = request.readBool();
			if(!isComplete)
			{
				// Any updates?
				if(newSelected.size() > 0)
				{
					match.broadcast(new TileSelectionExtendedMessage(player, newSelected));
				}
			}
			else
			{
				// Validate selection and concat letters
				StringBuilder letters = new StringBuilder(player.getSelection().size());
				Square previous = null;
				for(Square square : player.getSelection())
				{
					// Check tile + distance (anti-cheat)
					if(square.isTile() && (previous == null || (Math.abs(square.getColumn() - previous.getColumn()) <= 1 && Math.abs(square.getRow() - previous.getRow()) <= 1)))
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
				
				// Valid selection?
				if(letters != null && letters.length() > 0)
				{
					// Word formed?
					Word word = Word.parse(letters.toString());
					if(match.getLocale().getDictionary(DictionaryType.COMPLETE).contains(word))
					{
						// Assign points!
						int points = word.length();
						session.getMatchPlayer().modScore(+points);
						match.broadcast(new WordFoundMessage(session.getMatchPlayer(), word, +points));

                        List<Square> selectedTiles = player.getSelection();

                        // First freeze the current positions of the tiles so we can sent the correct over the network
                        for(Square square : selectedTiles)
                        {
                            square.freeze();
                        }

						// Clear the 'used' tiles
                        board.clearSquares(selectedTiles);

                        // Get the new tiles
						List<Square> newSquares = board.update();

                        // Send the cleared and new tiles
                        match.broadcast(new BoardUpdateMessage(selectedTiles, newSquares));

                        System.out.println(board.toMatrixString());
					}
				}
				
				// Clear selection 
				player.clearSelection();
				match.broadcast(new TileSelectionClearedMessage(player));
			}
		}
	}
}
