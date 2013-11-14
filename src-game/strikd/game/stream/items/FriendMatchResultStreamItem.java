package strikd.game.stream.items;

import strikd.game.stream.StreamPlayer;

public class FriendMatchResultStreamItem extends PlayerStreamItem
{
	public StreamPlayer loser;
	
	public String getSummary()
	{
		if(this.loser == StreamPlayer.SELF)
		{
			return String.format("%s defeated you", this.player);
		}
		else
		{
			return String.format("you defeated %s", this.loser);
		}
	}
}
