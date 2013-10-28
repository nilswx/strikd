package strikd.game.board.impl;

import strikd.game.board.Board;
import strikd.words.WordDictionary;

public class GravityBoard extends Board
{
	protected GravityBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}

	@Override
	public void fill()
	{
		// Find gaps, starting at the bottom. Gap found? Make the tile above it fall down
	}
}
