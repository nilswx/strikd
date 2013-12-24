package strikd.game.board.impl;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import strikd.game.board.AbstractTile;
import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.board.VirtualTile;
import strikd.locale.StaticLocale;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class RenegadeBoard extends Board
{
	public RenegadeBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	@Override
	public void update()
	{
		// Step through all the columns (left-to-right)
		int fillWidth = super.getWidth(), fillHeight = super.getHeight();
		for(int column = 0; column < fillWidth; column++)
		{
			List<Tile> col = super.getColumn(column);
			
			// Fill this column as much as possible
			while(col.size() < fillHeight)
			{
				// Locate a random word from here
				for(AbstractTile tile : this.findWordFromColumn(column, col))
				{
					// Tile not already on the board?
					if(tile instanceof VirtualTile)
					{
						super.addTile(tile.getColumn(), tile.getLetter());
					}
				}
			}
		}
	}
	
	private List<AbstractTile> findWordFromColumn(int column, List<Tile> col)
	{
		// Pick a starting point
		AbstractTile start;
		if(col.size() > 0)
		{
			start = col.get(col.size() - 1);
		}
		else
		{
			start = new VirtualTile(column, 0, RandomUtil.generateRandomLetter());
		}
		
		// Find/form a word starting from here
		List<AbstractTile> progress = Lists.newArrayList();
		boolean found = this.findWordStartingFrom(start, this.getDictionary().getIndex(), null, progress);
		
		// Evaluate result
		if(found)
		{
			System.out.println(progress);
			return progress;
		}
		else
		{
			return Collections.emptyList();
		}
	}
	
	private boolean findWordStartingFrom(AbstractTile src, LetterNode letter, Direction8 origin, List<AbstractTile> progress)
	{
		// Link is in the dictionary?
		LetterNode currentLetter = letter.node(src.getLetter());
		if(currentLetter != null)
		{
			// Push
			progress.add(src);
			
			// Word found?
			if(currentLetter.isWordEnd())
			{
				return true;
			}
			else
			{
				// Travel!
				nextDir: for(Direction8 dir : Direction8.noDiagonals())
				{
					// Don't go back!
					if(dir != origin)
					{
						// Determine coords
						int column = (src.getColumn() + dir.x);
						int row = (src.getRow() + dir.y);
						
						// Need a random/wildcard tile?
						AbstractTile tmp = super.getTile(column, row);
						if(tmp == null || currentLetter.node(tmp.getLetter()) == null)
						{
							// Was it invalid anyway?
							if(!super.isInRange(column, row))
							{
								continue nextDir;
							}
							else
							{
								// Pick a next random letter
								char nextRand = 0;
								for(char tmpLetter = 'A'; tmpLetter <= 'Z'; tmpLetter++)
								{
									if(currentLetter.node(tmpLetter) != null)
									{
										nextRand = tmpLetter;
										break;
									}
								}
								
								if(nextRand != 0)
								{
									tmp = new VirtualTile(column, row, nextRand);
								}
								else
								{
									continue nextDir;
								}
							}
						}
						
						// Word 'there'?
						boolean wordFound = this.findWordStartingFrom(tmp, currentLetter, dir.invert(), progress);
						if(wordFound)
						{
							return true;
						}
					}
				}
				
				// Pop
				progress.remove(progress.size() - 1);
			}
		}
		
		// Got a word so far?
		return letter.isWordEnd();
	}
	
	public static void main(String[] args)
	{
		RenegadeBoard b = new RenegadeBoard(6, 6, StaticLocale.getDictionary());
		b.update();
		System.out.println(b.toMatrixString());
	}
}
