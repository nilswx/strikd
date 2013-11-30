package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.game.stream.activity.ActivityStreamItem;
import strikd.game.stream.activity.FriendMatchResultStreamItem;
import strikd.game.stream.activity.ItemReceivedStreamItem;
import strikd.game.stream.activity.LevelUpStreamItem;
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
			super.writeLong(item.getTimestamp());
			this.writePlayer(self, item.getPlayer());
			if(item instanceof LevelUpStreamItem)
			{
				this.writeType(LevelUpStreamItem.TYPE);
				super.writeInt(((LevelUpStreamItem)item).getLevel());
			}
			else if(item instanceof ItemReceivedStreamItem)
			{
				this.writeType(ItemReceivedStreamItem.TYPE);
				super.writeStr(((ItemReceivedStreamItem)item).getItem().name());
			}
			else if(item instanceof FriendMatchResultStreamItem)
			{
				this.writeType(FriendMatchResultStreamItem.TYPE);
				this.writePlayer(self, ((FriendMatchResultStreamItem)item).getLoser());
			}
		}
	}
	
	private void writeType(String type)
	{
		// More compact (saves 2 bytes!)
		this.writeByte((byte)type.charAt(0));
	}
	
	private void writePlayer(Player self, Player player)
	{
		// Save bandwidth flag
		boolean isSelf = (player.getId() == self.getId());
		super.writeBool(isSelf);
		
		// Player data follows?
		if(!isSelf)
		{
			super.writeLong(player.getId());
			super.writeStr(player.getName());
			super.writeStr(player.getAvatar());
		}
	}
}
