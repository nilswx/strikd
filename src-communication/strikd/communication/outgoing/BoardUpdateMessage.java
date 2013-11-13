package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Tile;
import strikd.net.codec.OutgoingMessage;

public class BoardUpdateMessage extends OutgoingMessage
{
	public BoardUpdateMessage(List<Tile> removed, List<Tile> added)
	{
		super(Opcodes.Outgoing.BOARD_UPDATE);
		
		// Removed tiles
		super.writeByte((byte)removed.size());
		for(Tile tile : removed)
		{
			super.writeByte(tile.getTileId());
		}
		
		// New tiles (spawn at the top y and fall down till they hit something)
		super.writeByte((byte)added.size());
		for(Tile tile : added)
		{
			super.writeByte((byte)tile.getTileId());
			super.writeByte((byte)tile.getColumn());
			super.writeByte((byte)tile.getLetter());
			
			// TODO: store trigger info in remaining 3 bits (square.getLetter() << 5)); 
		}
	}
}
