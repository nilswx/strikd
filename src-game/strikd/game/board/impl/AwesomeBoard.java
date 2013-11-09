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

public class AwesomeBoard extends Board {

    private boolean isInitialUpdate = true;

    private static Logger logger = Logger.getLogger(AwesomeBoard.class);

    public AwesomeBoard(int width, int height, WordDictionary dictionary) {
        super(width, height, dictionary);
    }

    @Override
    public List<Square> update() {

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
        List<Square> newTiles = this.wildCardsOnBoard();

        // While there are gaps, fill it with the best word found
        Square square;
        while((square = this.getRandomWildCard()) != null)
        {
            this.addWordOnSquare(square);
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
            ArrayList<Square> column = this.squares[x];
            for(int y = column.size(); y < this.getHeight(); y++)
            {
                Square square = this.newSquare(x, y);
                square.setLetter(Square.WILDCARD_CHARACTER);
                column.add(square);
            }
        }
    }

    private Square getRandomWildCard()
    {
        ArrayList<Square> wildCards = this.wildCardsOnBoard();
        return RandomUtil.pickOne(wildCards);
    }

    private ArrayList<Square>wildCardsOnBoard()
    {
        ArrayList<Square> squares = new ArrayList<>();

        // Find all wildcards on board
        for(int x = 0; x < this.getWidth(); x++)
        {
            for(int y = 0; y < this.getHeight(); y++)
            {
                Square square = this.getSquare(x, y);
                if(square.getLetter() == Square.WILDCARD_CHARACTER)
                {
                    squares.add(square);
                }
            }
        }

        return squares;
    }

    private void addWordOnSquare(Square square)
    {
        IndexedWordSearch indexedWordSearch = new IndexedWordSearch(this);
        indexedWordSearch.setRequireWildCards(true);

        List<BoardWord> results = indexedWordSearch.findWordsForSquare(square);
        if(results != null && results.size() > 0)
        {
            // It is sorted already on score, so get the highest scoring one
            this.placeWord(results.get(0));
        }
        else
        {
            // Can't seem to fill anymore, doubt it will ever happen, fill it with random letters
            this.forceFillEmptySpaces();
        }
    }

    private void placeWord(BoardWord word)
    {
        logger.debug("Placing word: " + word);

        ArrayList<Square> squares = word.getSquares();
        for(Square square : squares)
        {
            Square currentSquare = this.getSquare(square.getColumn(), square.getRow());
            // Make sure we don't override existing squares
            if(currentSquare.getLetter() != square.getLetter())
            {
                if(currentSquare.getLetter() != Square.WILDCARD_CHARACTER)
                {
                    logger.error("Error, overwriting existing tile.");
                }

                // Place the correct letter on the correct position
                currentSquare.setLetter(square.getLetter());

                // Todo: Here we should determine if this letter should have a bomb or something
            }
        }
    }

    private void forceFillEmptySpaces()
    {
        for(int x = 0; x < this.getWidth(); x++)
        {
            for(int y = 0; y < this.getHeight(); y++)
            {
                Square square = this.getSquare(x, y);
                if(square.getLetter() == Square.WILDCARD_CHARACTER)
                {
                    char randomLetter = RandomUtil.randomCharFromString(IndexedWordSearch.ALPHABET);
                    square.setLetter(randomLetter);
                }
            }
        }
    }

}
