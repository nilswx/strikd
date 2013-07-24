package strikd.game.match.board;

import java.io.IOException;
import java.util.Random;

import strikd.game.match.board.tiles.Tile;
import strikd.game.match.board.triggers.BombTrigger;

public class SimpleBoard extends Board
{
	public SimpleBoard(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void regenerate()
	{
		super.clear();
		
		Random rand = new Random();
		this.setTile(new Tile(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()), 'K'));
		this.setTile(new Tile(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()), 'P', new BombTrigger()));
	}
	
	public static void main(String[] args) throws IOException
	{
		Board board = new SimpleBoard(5, 5);
		board.regenerate();
		
		System.out.println(board.toString());
		System.out.println();
		System.out.println(board.toLongString());
	}
}
