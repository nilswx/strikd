package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.player.Player;
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
			Player info = player.getInfo();
			super.writeByte((byte)0); // actor ID (virtual ID)
			super.writeStr(info.id.toString());
			super.writeStr(info.name);
			super.writeStr(info.avatar.toString());
			super.writeStr((info.fbIdentity != null) ? info.fbIdentity.country : "");
			super.writeInt(info.xp);
			super.writeInt(info.matches);
			super.writeInt(info.wins);
		}
	}
}
