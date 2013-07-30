package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.board.Board;
import strikd.net.codec.OutgoingMessage;

public class BoardUpdateMessage extends OutgoingMessage
{
	public BoardUpdateMessage(int version, Board board)
	{
		super(Opcodes.Outgoing.BOARD_UPDATE);
		super.writeInt(version);
		
		// TODO: define update protocol
	}
}
