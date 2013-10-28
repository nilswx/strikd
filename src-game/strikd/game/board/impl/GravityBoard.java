package strikd.game.board.impl;

import strikd.game.board.Board;
import strikd.words.WordDictionary;

public class GravityBoard extends Board
{
	public GravityBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	@Override
	public void fill()
	{
		// Run through all tiles, start at the bottom left (0,0)
		int width = this.getWidth(), height = this.getHeight();
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				// No tile here?
				if(this.squares[x][y].isNull())
				{
					// Letter tile above?
					if(y < (height - 1) && this.squares[x][y + 1].isTile())
					{
						// Fall down!
						this.squares[x][y].setLetter(this.squares[x][y + 1].getLetter());
						this.squares[x][y + 1].clear();
					}
				}
			}
		}
		
		// Start at lowest gaps, fill gaps with words (use the surrounding tiles)
		for(int x = 0; x < width; x++)
		{
			for(int y = (height - 1); y >= 0; y--)
			{
				if(this.squares[x][y].isNull())
				{
					
				}
			}
		}
	}
}
