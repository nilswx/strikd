package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.news.NewsItem;
import strikd.net.codec.OutgoingMessage;

public class NewsMessage extends OutgoingMessage
{
	public NewsMessage(List<NewsItem> items)
	{
		super(Opcodes.Outgoing.NEWS);
		super.writeInt(items.size());
		for(NewsItem item : items)
		{
			super.writeStr(item.getHeadline());
			super.writeLong(item.getTimestamp());
			super.writeStr(item.getImageUrl());
			super.writeStr(item.getBody());
		}
	}
}
