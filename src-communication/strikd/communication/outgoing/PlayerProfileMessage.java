package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.PlayerProfile;
import strikd.net.codec.OutgoingMessage;

public class PlayerProfileMessage extends OutgoingMessage
{
	public PlayerProfileMessage(PlayerProfile profile)
	{
		super(Opcodes.Outgoing.PLAYER_PROFILE);
		super.writeStr(profile.playerId.toString());
		super.writeStr(profile.playerName);
		super.writeStr(profile.avatar.toString());
		super.writeLong(profile.playerId.getTime());
	}
}
