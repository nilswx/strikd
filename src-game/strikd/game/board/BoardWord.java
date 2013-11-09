package strikd.game.board;

import java.util.ArrayList;

public class BoardWord implements Comparable {

    private static final int LENGTH_SCORE = -5;
    private static final int WILDCARD_SCORE = 0;

    private static final int NORTH_SCORE = -2;
    private static final int EAST_SCORE = 4;
    private static final int SOUTH_SCORE = 3;
    private static final int WEST_SCORE = -2;

    private static final int SAME_DIRECTION_SCORE = 4;

    private ArrayList<Square> squares;

    public BoardWord(ArrayList<Square> squares)
    {
        this.squares = squares;
    }

    private int score = Integer.MIN_VALUE;

    public String toString()
    {
        StringBuilder wordBuilder = new StringBuilder();
        for(Square square : squares)
        {
            wordBuilder.append(square.getLetter());
        }
        return "Word: " + wordBuilder.toString() + " VALUE " + this.getScore();
    }

    public ArrayList<Square> getSquares()
    {
        return this.squares;
    }

    public int getScore()
    {
        // Some basic caching here, just calculating it on the first run
        if(this.score == Integer.MIN_VALUE)
        {
            // Score for lengths
            int currentScore = this.squares.size() * LENGTH_SCORE;
            for(int i = 0; i < this.squares.size(); i++)
            {
                Square square = this.squares.get(i);
                // Score for wildcard
                if(square.getClass() == WildCardSquare.class)
                {
                    currentScore += WILDCARD_SCORE;
                }

                // Detect the changes in direction
                if(i > 0)
                {
                    // Points for delta
                    Square lastSquare = this.squares.get(i - 1);
                    int deltaX = square.getColumn() - lastSquare.getColumn();
                    int deltaY = square.getRow() - lastSquare.getRow();

                    // Todo: Improve this
                    Direction8 direction = Direction8.directionFromDelta(deltaX, deltaY);
                    if(direction == Direction8.North)
                    {
                        currentScore += NORTH_SCORE;
                    }
                    else if(direction == Direction8.East)
                    {
                        currentScore += EAST_SCORE;
                    }
                    else if(direction == Direction8.South)
                    {
                        currentScore += SOUTH_SCORE;
                    }
                    else if(direction == Direction8.West)
                    {
                        currentScore += WEST_SCORE;
                    }

                    // Points for keeping in the same direction
                    if(i > 1)
                    {
                        Square beforeLastSquare = this.squares.get(i - 2);
                        if((square.getColumn() == lastSquare.getColumn() && lastSquare.getColumn() == beforeLastSquare.getColumn()) || (square.getRow() == lastSquare.getRow() && lastSquare.getRow() == beforeLastSquare.getRow()))
                        {
                            currentScore += SAME_DIRECTION_SCORE;
                        }
                    }
                }
            }

            this.score = currentScore;
        }
        return this.score;
    }

    public boolean containsWildCards()
    {
        for(Square square : this.squares)
        {
            if(square.getClass() == WildCardSquare.class)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object object) {
        if(object.getClass() == BoardWord.class)
        {
            BoardWord compareWord = (BoardWord)object;
            return compareWord.getScore() - this.getScore();
        }
        // At default it sorts higher than non BoardWord objects
        return 1;
    }
}
