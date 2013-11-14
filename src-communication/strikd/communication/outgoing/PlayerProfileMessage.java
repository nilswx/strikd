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
		super.writeInt(profile.matches);
		super.writeInt(profile.wins);
		super.writeInt(profile.losses);
		super.writeLong(profile.playerId.getTime());
		super.writeLong(profile.lastOnline.getTime());
	}
}
