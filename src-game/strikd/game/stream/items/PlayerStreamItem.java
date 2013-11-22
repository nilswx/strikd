package strikd.game.stream.items;

import strikd.game.stream.ActivityStreamItem;
import strikd.game.stream.StreamPlayer;

public abstract class PlayerStreamItem extends ActivityStreamItem
{
	public StreamPlayer player;
	
	public boolean isAboutSelf()
	{
		return (this.player == StreamPlayer.SELF);
	}
	
	public boolean isAboutFriend()
	{
		return !this.isAboutSelf();
	}
}
