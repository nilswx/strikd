package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import strikd.game.player.Player;

@Entity @DiscriminatorValue(FriendMatchResultStreamItem.TYPE)
public class FriendMatchResultStreamItem extends ActivityStreamItem
{
	@ManyToOne(optional=true)
	private Player loser;
	
	public Player getLoser()
	{
		return this.loser;
	}

	public void setLoser(Player loser)
	{
		this.loser = loser;
	}
	
	public static final String TYPE = "m";
}
