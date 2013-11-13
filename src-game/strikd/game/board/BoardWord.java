package strikd.game.board;

import java.util.ArrayList;

public class BoardWord implements Comparable<BoardWord>
{
    private static final int LENGTH_SCORE = -5;
    private static final int WILDCARD_SCORE = 0;

    private static final int NORTH_SCORE = -2;
    private static final int EAST_SCORE = 4;
    private static final int SOUTH_SCORE = 3;
    private static final int WEST_SCORE = -2;

    private static final int SAME_DIRECTION_SCORE = 4;

    private final ArrayList<Tile> tiles;

    public BoardWord(ArrayList<Tile> tiles)
    {
        this.tiles = tiles;
    }

    private int score = Integer.MIN_VALUE;

    public String toString()
    {
        StringBuilder wordBuilder = new StringBuilder();
        for(Tile tile : tiles)
        {
            wordBuilder.append(tile.getLetter());
        }
        return "Word: " + wordBuilder.toString() + " VALUE " + this.getScore();
    }

    public ArrayList<Tile> getTiles()
    {
        return this.tiles;
    }

    public int getScore()
    {
        // Some basic caching here, just calculating it on the first run
        if(this.score == Integer.MIN_VALUE)
        {
            // Score for lengths
            int currentScore = this.tiles.size() * LENGTH_SCORE;
            for(int i = 0; i < this.tiles.size(); i++)
            {
                Tile tile = this.tiles.get(i);
                
                // Score for wildcard
                /*
                if(tile.getClass() == WildCardSquare.class)
                {
                    currentScore += WILDCARD_SCORE;
                }*/

                // Detect the changes in direction
                if(i > 0)
                {
                    // Points for delta
                    Tile lastSquare = this.tiles.get(i - 1);
                    int deltaX = tile.getColumn() - lastSquare.getColumn();
                    int deltaY = tile.getRow() - lastSquare.getRow();

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
                        Tile beforeLastSquare = this.tiles.get(i - 2);
                        if((tile.getColumn() == lastSquare.getColumn() && lastSquare.getColumn() == beforeLastSquare.getColumn()) || (tile.getRow() == lastSquare.getRow() && lastSquare.getRow() == beforeLastSquare.getRow()))
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
        /*for(Tile tile : this.tiles)
        {
            if(tileWildCardSquare.class)
            {
                return true;
            }
        }*/
        return false;
    }

    @Override
    public int compareTo(BoardWord other)
    {
    	return other.getScore() - this.getScore();
    }
}
