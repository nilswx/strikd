package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class AnnounceMatchMessage extends OutgoingMessage
{
	public AnnounceMatchMessage(Match match, MatchPlayer player, MatchPlayer opponent)
	{
		super(Opcodes.Outgoing.ANNOUNCE_MATCH);
		super.writeLong(match.getMatchId());
		super.writeByte((byte)player.getActorId());
		super.writeByte((byte)opponent.getActorId());
		UserInfoMessage.serializeUser(opponent.getInfo(), this);
		super.writeInt(match.getTimer().getTimeLeft());
		super.writeByte((byte)match.getBoard().getWidth());
		super.writeByte((byte)match.getBoard().getHeight());
		super.writeByte(match.getLoadingTime()); // "Loading..." time in seconds
	}
}
