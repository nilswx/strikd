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
		super.writeTime(null);
	}
	
	public static void serializePlayer(Player player, OutgoingMessage msg)
	{
		msg.writeInt(player.getId());
		msg.writeStr(player.getName());
		msg.writeStr(player.getAvatarData());
		msg.writeStr(player.getMotto());
		msg.writeStr(player.getCountry());
		msg.writeInt(player.getXp());
		msg.writeInt(player.getLevel());
		msg.writeInt(player.getMatches());
		msg.writeInt(player.getWins());
		msg.writeInt(player.getLosses());
	}
}
