package strikd.game.board;

import strikd.communication.outgoing.SquareUpdatesMessage;
import strikd.game.board.tiles.Square;

public class BoardUpdateGenerator
{
	private int updateCount;
	private final Board board;
	
	public BoardUpdateGenerator(Board board)
	{
		this.board = board; 
	}
	
	public void registerUpdate()
	{
		this.updateCount++;
	}
	
	public boolean hasUpdates()
	{
		return (this.updateCount > 0);
	}
	
	public synchronized SquareUpdatesMessage generateUpdates()
	{
		// Start the message with the amount of included updates
		SquareUpdatesMessage msg = new SquareUpdatesMessage(this);
		msg.appendCount(this.updateCount);
		
		// Append the updated tiles and clear their flag
		outer: for(int x = 0; x < this.board.getWidth(); x++)
		{
			for(int y = 0; y < this.board.getHeight(); y++)
			{
				Square sq = (Square)this.board.squares[x][y];
				if(sq.needsUpdate())
				{
					msg.appendUpdate(sq);
					sq.updated();
					
					if(--this.updateCount == 0)
					{
						break outer;
					}
				}
			}
		}
		
		// Return the generated message for broadcasting
		return msg;
	}
}
