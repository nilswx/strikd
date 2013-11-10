package strikd.game.board;

public class WildCardSquare extends Tile {

    public WildCardSquare(int x, int y, Board board) {
        super(x, y, board);
        this.y = y;
    }

    public void setLetter(char letter)
    {
        this.letter = letter;
    }

    public int getRow()
    {
        return this.y;
    }

}
