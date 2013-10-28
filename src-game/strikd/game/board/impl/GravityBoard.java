package strikd.game.board.impl;

import strikd.words.WordDictionary;

public class GravityBoard extends BruteBoard
{
	public GravityBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	@Override
	public void update()
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
		
		// Fill the gaps
		super.update();
	}
}
