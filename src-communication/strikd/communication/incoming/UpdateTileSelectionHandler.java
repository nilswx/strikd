package strikd.communication.incoming;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.TileSelectionClearedMessage;
import strikd.communication.outgoing.TileSelectionExtendedMessage;
import strikd.communication.outgoing.WordFoundMessage;
import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;
import strikd.words.Word;

public class UpdateTileSelectionHandler extends MessageHandler
{
	private static final Logger logger = LoggerFactory.getLogger(UpdateTileSelectionHandler.class);
	
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
			List<Tile> newSelected = new ArrayList<Tile>();
			for(int i = 0; i < amount; i++)
			{
                Tile tile = board.getTile(request.readByte());
				if(tile != null)
				{
                    player.selectTile(tile);
                    newSelected.add(tile);
				}
			}
			
			// Was this the last batch?
			boolean isComplete = request.readBool();
			if(!isComplete)
			{
				// Any updates?
				if(newSelected.size() > 0)
				{
					// Just to opponent
					match.getOpponent(player).send(new TileSelectionExtendedMessage(player, newSelected));
				}
			}
			else
			{
				// Validate selection and concat letters
				StringBuilder letters = new StringBuilder(player.getSelection().size());
				for(Tile tile : player.getSelection())
				{
					letters.append(tile.getLetter());
				}
				
				// FIXME: Check tile + distance (anti-cheat), getRow() returns old values?
				/*
				if(previous == null || (Math.abs(tile.getColumn() - previous.getColumn()) <= 1 && Math.abs(tile.getRow() - previous.getRow()) <= 1))
				{
					letters.append(tile.getLetter());
					previous = tile;
				}
				else
				{
					// Invalid selection
					letters = null;
					logger.debug("invalid selection, distance too big ({} -> {})", previous, tile);
					break;
				}*/
				
				// Valid selection?
				if(letters != null && letters.length() > 0)
				{
					// Word formed?
					Word word = Word.parse(letters.toString());
					if(match.getLocale().getDictionary(DictionaryType.COMPLETE).contains(word))
					{
						// Word correct!
						int points = word.length();
						logger.debug("found '{}' ({} points)", word, points);
						
						// Assign points
						session.getMatchPlayer().modScore(+points);
						match.broadcast(new WordFoundMessage(session.getMatchPlayer(), word, +points));

						// Expend the 'used' tiles
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
					else
					{
						logger.debug("unknown word '{}'", word);
					}
				}
				
				// Clear selection 
				player.clearSelection();
				match.broadcast(new TileSelectionClearedMessage(player));
			}
		}
	}
}
