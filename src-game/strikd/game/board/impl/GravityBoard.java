package strikd.game.board.impl;

import java.util.List;

import com.google.common.collect.Lists;

import strikd.communication.outgoing.BoardUpdateMessage;
import strikd.game.board.Square;
import strikd.words.WordDictionary;

public class GravityBoard extends BruteBoard
{
	protected final List<Square> removed = Lists.newArrayList();
	protected final List<Square> added = Lists.newArrayList();
	
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
						this.squares[x][y + 1].clear();
						this.squares[x][y].setLetter(this.squares[x][y + 1].getLetter());
					}
				}
			}
		}
		
		// Fill the gaps
		super.update();
		
		// Plug the remaining holes
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < width; y++)
			{
				if(this.squares[x][y].isNull())
				{
					this.squares[x][y].setLetter('A'); // TODO: random
				}
			}
		}
	}
	
	public BoardUpdateMessage generateUpdateMessage()
	{
		// Generate update message
		BoardUpdateMessage msg = new BoardUpdateMessage(this.removed, this.added);
		
		// Clear updates
		this.removed.clear();
		this.added.clear();
		
		// Broadcast material!
		return msg;
	}
}
