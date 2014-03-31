package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class MatchEndedMessage extends OutgoingMessage
{
	public MatchEndedMessage(MatchPlayer p1, MatchPlayer p2, MatchPlayer winner)
	{
		super(Opcodes.Outgoing.MATCH_ENDED);
		this.serializePlayer(p1);
		this.serializePlayer(p2);
		super.writeByte((byte)(winner != null ? winner.getActorId() : -1));
	}
	
	private void serializePlayer(MatchPlayer player)
	{
		super.writeByte(player.getActorId());
		super.writeInt(player.getFoundWords().size());
		super.writeInt(player.getLetterCount());
	}
}
