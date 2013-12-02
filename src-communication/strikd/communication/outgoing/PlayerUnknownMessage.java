package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class PlayerUnknownMessage extends OutgoingMessage
{
	public PlayerUnknownMessage(int playerId)
	{
		super(Opcodes.Outgoing.PLAYER_UNKNOWN);
		super.writeInt(playerId);
	}
}
