package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;

public class AnnounceMatchMessage extends OutgoingMessage
{
	public AnnounceMatchMessage(Match match)
	{
		super(Opcodes.Outgoing.NOP);
		
		super.writeLong(match.getMatchId());
		super.writeByte((byte)match.getBoard().getDimension());
		super.writeByte((byte)0); // music ID
		super.writeByte((byte)5); // "Loading..." time in seconds
		
		super.writeByte((byte)match.getPlayers().length);
		for(MatchPlayer player : match.getPlayers())
		{
			User user = player.getInfo();
			super.writeByte((byte)0); // actor ID (virtual ID)
			super.writeStr(user.id.toString());
			super.writeStr(user.name);
			super.writeStr(user.avatar.toString());
			super.writeStr((user.fbIdentity != null) ? user.fbIdentity.country : "");
			super.writeInt(user.xp);
			super.writeInt(user.matches);
			super.writeInt(user.wins);
		}
	}
}
