package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.PlayerProfile;
import strikd.net.codec.OutgoingMessage;

public class PlayerProfileMessage extends OutgoingMessage
{
	public PlayerProfileMessage(PlayerProfile profile)
	{
		super(Opcodes.Outgoing.PLAYER_PROFILE);
		super.writeStr(profile.playerId);
		super.writeStr(profile.name);
		super.writeStr(profile.avatar);
		super.writeInt(profile.matches);
		super.writeInt(profile.wins);
		super.writeInt(profile.losses);
		super.writeLong(0);
		super.writeLong(profile.lastOnline);
	}
}
