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
		
		// Match data
		super.writeLong(match.getMatchId());
		super.writeStr(match.getLocale().getLocale());
		super.writeInt(match.getTimer().getDuration());
		
		// MatchPlayerID of self
		super.writeByte((byte)player.getPlayerId());
		
		// Data of opponent
		super.writeByte((byte)opponent.getPlayerId());
		PlayerInfoMessage.serializePlayer(opponent.getInfo(), this);
		
		 // "Loading..." time in seconds
		super.writeByte(match.getLoadingTime());
	}
}
