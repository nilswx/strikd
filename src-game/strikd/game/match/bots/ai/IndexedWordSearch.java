package strikd.game.match.bots.ai;

import strikd.game.board.*;
import strikd.util.RandomUtil;
import strikd.words.index.LetterNode;
import strikd.words.index.WordDictionaryIndex;

import java.util.*;

public class IndexedWordSearch {

    private Board board;
    private HashMap<Integer, BoardWord> boardWords;

    private static Direction8[] SEARCH_DIRECTIONS = Direction8.all();
    public static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Settings
    private boolean allowWildcards = true;
    private boolean requireWildCards = true;

    private int minWordSize = 4;
    private int maxWordSize = 9;
    private int wordLimit = 10;

    public IndexedWordSearch(Board board)
    {
        this.board = board;
    }

    // The actual searching
    public List<BoardWord> findWordsForSquare(Tile tile)
    {
        WordDictionaryIndex wordDictionaryIndex = this.board.getDictionary().getIndex();
        this.boardWords = new HashMap<>();

        // Find a neat list of words for this square
        Stack<Tile> progressStack = new Stack<>();
        this.findWordsWithStartingSquare(tile, wordDictionaryIndex, null, progressStack);

        // Sort the result
        List<BoardWord> sortedList = new ArrayList<BoardWord>(this.boardWords.values());
        Collections.sort(sortedList);
        return sortedList;
    }

    private void findWordsWithStartingSquare(Tile currentSquare, LetterNode branch, Direction8 origin, Stack<Tile> progressStack)
    {
        // Keep track of the max amount of words to search for (they are unique words)
        if(this.boardWords.size() >= this.wordLimit)
        {
            return;
        }

        // The questionmarks are used as wildcards while finding words
        if(currentSquare.getLetter() == Tile.WILDCARD_CHARACTER && currentSquare.getClass() != WildCardSquare.class)
        {
            // When wildcards are allow we create WildCardSquares for each letter in the alphabet!
            if(this.allowWildcards == true)
            {
                // This prevents from always returning words starting with the same letters
                String randomizedAlphabet = RandomUtil.randomizeString(ALPHABET);

                for(int i = 0; i < randomizedAlphabet.length(); i++)
                {
                    char letter = randomizedAlphabet.charAt(i);
                    WildCardSquare wildCardSquare = new WildCardSquare(currentSquare.getColumn(), currentSquare.getRow(), currentSquare.getBoard());
                    wildCardSquare.setLetter(letter);
                    this.findWordsWithStartingSquare(wildCardSquare, branch, origin, progressStack);
                }
            }
        }
        else
        {
            LetterNode currentLetterBranch = branch.node(currentSquare.getLetter());
            if(currentLetterBranch != null)
            {
                // Keep track of progress
                progressStack.push(currentSquare);

                if(progressStack.size() <= this.maxWordSize)
                {
                    // Woah it is a word!
                    if(currentLetterBranch.isWordEnd() && progressStack.size() >= this.minWordSize)
                    {
                        this.foundWord(progressStack);
                    }

                    // Maybe there are more, continue searching
                    for(Direction8 direction : SEARCH_DIRECTIONS)
                    {
                        if(direction != origin)
                        {
                            if(direction != Direction8.NorthEast && direction != Direction8.SouthEast && direction != Direction8.NorthWest && direction != Direction8.SouthWest)
                            {
                                // Try to get next tile at new direction
                                Tile nextSquare = this.board.getTile(currentSquare.getColumn() + direction.x, currentSquare.getRow() + direction.y);
                                if(nextSquare != null && nextSquare.isTile() && !this.visitedSquarePosition(nextSquare, progressStack))
                                {
                                    this.findWordsWithStartingSquare(nextSquare, currentLetterBranch, direction.invert(), progressStack);
                                }
                            }
                        }
                    }
                }
                progressStack.pop();
            }
        }
    }

    private void foundWord(Stack<Tile> progressStack)
    {
        ArrayList<Tile> tiles = new ArrayList<>();
        for(Tile tile : progressStack)
        {
            tiles.add(tile);
        }

        BoardWord boardWord = new BoardWord(tiles);
        if(this.requireWildCards && boardWord.containsWildCards())
        {
            this.boardWords.put(boardWord.toString().hashCode(), boardWord);
        }
    }

    private boolean visitedSquarePosition(Tile tile, Stack<Tile> progessStack)
    {
        for(Tile matchSquare : progessStack)
        {
            if(tile.getColumn() == matchSquare.getColumn() && tile.getRow() == matchSquare.getRow())
            {
                return true;
            }
        }
        return false;
    }

    // Some getters and settters
    public void setAllowWildcards(boolean allowWildcards)
    {
        this.allowWildcards = allowWildcards;
    }

    public int getWordLimit() {
        return wordLimit;
    }

    public void setWordLimit(int wordLimit) {
        this.wordLimit = wordLimit;
    }

    public int getMinWordSize() {
        return minWordSize;
    }

    public void setMinWordSize(int minWordSize) {
        this.minWordSize = minWordSize;
    }

    public int getMaxWordSize() {
        return maxWordSize;
    }

    public void setMaxWordSize(int maxWordSize) {
        this.maxWordSize = maxWordSize;
    }

    public void setRequireWildCards(boolean requireWildCards)
    {
        if(requireWildCards == true)
        {
            this.setAllowWildcards(requireWildCards);
        }
        this.requireWildCards = requireWildCards;
    }

    public boolean requireWildCards()
    {
        return this.requireWildCards;
    }

}
