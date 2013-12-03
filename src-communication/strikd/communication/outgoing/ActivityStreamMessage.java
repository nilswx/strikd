package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.items.ItemType;
import strikd.game.player.Player;
import strikd.game.stream.ActivityStreamItem;
import strikd.game.stream.activity.FriendMatchResultStreamItem;
import strikd.game.stream.activity.ItemReceivedStreamItem;
import strikd.game.stream.activity.LevelUpStreamItem;
import strikd.game.stream.activity.PlayerJoinedStreamItem;
import strikd.net.codec.OutgoingMessage;

public class ActivityStreamMessage extends OutgoingMessage
{
	public ActivityStreamMessage(int start, int end, List<ActivityStreamItem> items, Player self)
	{
		super(Opcodes.Outgoing.ACTIVITY_STREAM);
		
		// Write range
		super.writeInt(start);
		super.writeInt(end);
		
		// Serialize items
		super.writeInt(items.size());
		for(ActivityStreamItem item : items)
		{
			super.writeTime(item.getTimestamp());
			super.writeInt(item.getPlayer().getId());
			if(item instanceof LevelUpStreamItem)
			{
				this.writeType(LevelUpStreamItem.TYPE);
				super.writeInt(((LevelUpStreamItem)item).getLevel());
			}
			else if(item instanceof ItemReceivedStreamItem)
			{
				this.writeType(ItemReceivedStreamItem.TYPE);
				
				ItemType itemType = ((ItemReceivedStreamItem)item).getItem();
				super.writeStr(itemType != null ? itemType.getCode() : null);
			}
			else if(item instanceof FriendMatchResultStreamItem)
			{
				this.writeType(FriendMatchResultStreamItem.TYPE);
				this.writeInt(((FriendMatchResultStreamItem)item).getLoser().getId());
			}
			else if(item instanceof PlayerJoinedStreamItem)
			{
				this.writeType(PlayerJoinedStreamItem.TYPE);
			}
		}
	}
	
	private void writeType(String type)
	{
		// More compact (saves 2 bytes!)
		this.writeByte((byte)type.charAt(0));
	}
}
