package strikd.game.match;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.outgoing.TileSelectionClearedMessage;
import strikd.communication.outgoing.WordFoundMessage;
import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.words.Word;

public class SelectionValidator
{
	private static final Logger logger = LoggerFactory.getLogger(SelectionValidator.class);
	
	public static void validateSelection(MatchPlayer player)
	{
		// Validate selection and concat letters
		StringBuilder letters = new StringBuilder(player.getSelection().size());
		for(Tile tile : player.getSelection())
		{
			letters.append(tile.getLetter());
			// TODO: anti-cheat rangecheck
		}
		
		// Word formed?
		Match match = player.getMatch();
		Word word = Word.parse(letters.toString());
		if(!match.getLocale().getDictionary(DictionaryType.COMPLETE).contains(word))
		{
			logger.debug("{} selected unknown word '{}'", player, word);
		}
		else
		{
			// Word correct!
			int points = word.length();
			logger.debug("{} found '{}' ({} points)", player, word, points);
			
			// Assign points
			player.modScore(+points);
			match.broadcast(new WordFoundMessage(player, word, +points));

			// Expend the 'used' tiles
			Board board = match.getBoard();
			for(Tile tile : player.getSelection())
			{
				// Fire trigger!
				if(tile.hasTrigger())
				{
					tile.getTrigger().execute(player);
				}
				board.removeTile(tile);
			}

            // Generate updates
			board.update();
            match.broadcast(board.generateUpdateMessage());
		}

		// In all cases: clear selection
		player.clearSelection();
		match.broadcast(new TileSelectionClearedMessage(player));
	}
}
