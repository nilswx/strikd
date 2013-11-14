package strikd.game.stream.items;

import strikd.game.stream.EventStreamItem;
import strikd.game.stream.StreamPlayer;

public abstract class PlayerStreamItem extends EventStreamItem
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
