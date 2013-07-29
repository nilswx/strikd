package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class PlayerInfoMessage extends OutgoingMessage
{
	public PlayerInfoMessage(Player p)
	{
		super(Opcodes.Outgoing.PLAYER_INFO);
		super.writeStr(p.id.toString());
		super.writeStr(p.name);
	}
}
