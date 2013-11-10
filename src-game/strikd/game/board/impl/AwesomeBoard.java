package strikd.game.board.impl;

import com.google.common.base.Stopwatch;
import org.apache.log4j.Logger;
import strikd.game.board.*;

import strikd.game.match.bots.ai.IndexedWordSearch;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AwesomeBoard extends Board
{
	private boolean isInitialUpdate = true;

	private static Logger logger = Logger.getLogger(AwesomeBoard.class);

	public AwesomeBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}

	@Override
	public List<Tile> update()
	{

		Stopwatch stopwatch = new Stopwatch();
		stopwatch.reset();
		stopwatch.start();

		if(this.isInitialUpdate)
		{
			this.clear();
		}

		// The letters who'm have been removed are made wildcard letters
		this.createWildCards();

		// Keep track of the tiles which will be filled
		List<Tile> newTiles = this.wildCardsOnBoard();

		// While there are gaps, fill it with the best word found
		Tile tile;
		while((tile = this.getRandomWildCard()) != null)
		{
			this.addWordOnSquare(tile);
		}

		stopwatch.stop();

		System.out.println(String.format("Time taken to fill board: %d ms (%d microseconds)", stopwatch.elapsedMillis(), stopwatch.elapsedTime(TimeUnit.MICROSECONDS)));

		this.isInitialUpdate = false;

		return newTiles;
	}

	private void createWildCards()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			List<Tile> column = this.columns[x];
			for(int y = column.size(); y < this.getHeight(); y++)
			{
				Tile tile = this.newTile(x, y);
				tile.setLetter(Tile.WILDCARD_CHARACTER);
				column.add(tile);
			}
		}
	}

	private Tile getRandomWildCard()
	{
		ArrayList<Tile> wildCards = this.wildCardsOnBoard();
		return RandomUtil.pickOne(wildCards);
	}

	private ArrayList<Tile> wildCardsOnBoard()
	{
		ArrayList<Tile> tiles = new ArrayList<>();

		// Find all wildcards on board
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				Tile tile = this.getTile(x, y);
				if(tile.getLetter() == Tile.WILDCARD_CHARACTER)
				{
					tiles.add(tile);
				}
			}
		}

		return tiles;
	}

	private void addWordOnSquare(Tile tile)
	{
		IndexedWordSearch indexedWordSearch = new IndexedWordSearch(this);
		indexedWordSearch.setRequireWildCards(true);

		List<BoardWord> results = indexedWordSearch.findWordsForSquare(tile);
		if(results != null && results.size() > 0)
		{
			// It is sorted already on score, so get the highest scoring one
			this.placeWord(results.get(0));
		}
		else
		{
			// Can't seem to fill anymore, doubt it will ever happen, fill it
			// with random letters
			this.forceFillEmptySpaces();
		}
	}

	private void placeWord(BoardWord word)
	{
		logger.debug("Placing word: " + word);

		ArrayList<Tile> tiles = word.getTiles();
		for(Tile tile : tiles)
		{
			Tile currentSquare = this.getTile(tile.getColumn(), tile.getRow());
			// Make sure we don't override existing squares
			if(currentSquare.getLetter() != tile.getLetter())
			{
				if(currentSquare.getLetter() != Tile.WILDCARD_CHARACTER)
				{
					logger.error("Error, overwriting existing tile.");
				}

				// Place the correct letter on the correct position
				currentSquare.setLetter(tile.getLetter());

				// Todo: Here we should determine if this letter should have a
				// bomb or something
			}
		}
	}

	private void forceFillEmptySpaces()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				Tile tile = this.getTile(x, y);
				if(tile.getLetter() == Tile.WILDCARD_CHARACTER)
				{
					char randomLetter = RandomUtil.randomCharFromString(IndexedWordSearch.ALPHABET);
					tile.setLetter(randomLetter);
				}
			}
		}
	}

}
