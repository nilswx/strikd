package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class PlayerInfoMessage extends OutgoingMessage
{
	public PlayerInfoMessage(Player player)
	{
		super(Opcodes.Outgoing.PLAYER_INFO);
		serializePlayer(player, this);
	}
	
	public static void serializePlayer(Player player, OutgoingMessage msg)
	{
		msg.writeStr(player.id);
		msg.writeStr(player.name);
		msg.writeStr(player.avatar);
		msg.writeStr(player.country);
		msg.writeInt(player.xp);
		msg.writeInt(player.matches);
		msg.writeInt(player.wins);
	}
}
