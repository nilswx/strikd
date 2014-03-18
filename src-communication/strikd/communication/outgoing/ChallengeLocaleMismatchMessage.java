package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeLocaleMismatchMessage extends OutgoingMessage
{
	public ChallengeLocaleMismatchMessage(Player player, String requiredLocale)
	{
		super(Opcodes.Outgoing.CHALLENGE_LOCALE_MISMATCH);
		super.writeInt(player.getId());
		super.writeStr(requiredLocale);
	}
}
