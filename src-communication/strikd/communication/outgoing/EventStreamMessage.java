package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.stream.EventStreamItem;
import strikd.game.stream.StreamPlayer;
import strikd.game.stream.items.FriendMatchResultStreamItem;
import strikd.game.stream.items.ItemReceivedStreamItem;
import strikd.game.stream.items.LevelUpStreamItem;
import strikd.game.stream.items.NewsStreamItem;
import strikd.net.codec.OutgoingMessage;

public class EventStreamMessage extends OutgoingMessage
{
	private static final byte TYPE_NEWS = 1;
	private static final byte TYPE_LEVEL_UP = 2;
	private static final byte TYPE_ITEM_RECEIVED = 3;
	private static final byte TYPE_FRIEND_BEATED = 4;
	
	public EventStreamMessage(long periodBegin, long periodEnd, List<EventStreamItem> items)
	{
		super(Opcodes.Outgoing.EVENT_STREAM);
		super.writeLong(periodBegin);
		super.writeLong(periodEnd);
		super.writeInt(items.size());
		for(EventStreamItem item : items)
		{
			if(item instanceof NewsStreamItem)
			{
				this.writeNews((NewsStreamItem)item);
			}
			else if(item instanceof LevelUpStreamItem)
			{
				this.writeLevelUp((LevelUpStreamItem)item);
			}
			else if(item instanceof ItemReceivedStreamItem)
			{
				this.writeItemReceived((ItemReceivedStreamItem)item);
			}
			else if(item instanceof FriendMatchResultStreamItem)
			{
				this.writeFriendMatchResult((FriendMatchResultStreamItem)item);
			}
			super.writeLong(item.timestamp);
		}
	}
	
	private void writeNews(NewsStreamItem news)
	{
		super.writeByte(TYPE_NEWS);
		super.writeStr(news.headline);
		super.writeStr(news.imageUrl);
		super.writeStr(news.body);
	}
	
	private void writeLevelUp(LevelUpStreamItem levelUp)
	{
		this.writeByte(TYPE_LEVEL_UP);
		this.writePlayer(levelUp.player);
		this.writeByte((byte)levelUp.level);
	}
	
	private void writeItemReceived(ItemReceivedStreamItem itemReceived)
	{
		this.writeByte(TYPE_ITEM_RECEIVED);
		this.writePlayer(itemReceived.player);
		this.writeStr(itemReceived.item.name());
	}
	
	private void writeFriendMatchResult(FriendMatchResultStreamItem result)
	{
		this.writeByte(TYPE_FRIEND_BEATED);
		this.writePlayer(result.player);
		this.writePlayer(result.loser);
	}
	
	private void writePlayer(StreamPlayer player)
	{
		if(player == StreamPlayer.SELF)
		{
			super.writeStr(null);
		}
		else
		{
			super.writeStr(player.playerId);
			super.writeStr(player.realName);
			super.writeStr(player.avatar);
		}
	}
}
