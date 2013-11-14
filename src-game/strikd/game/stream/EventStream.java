package strikd.game.stream;

import java.util.Collection;
import java.util.LinkedList;

import com.google.common.collect.Lists;

public class EventStream
{
	private final LinkedList<EventStreamItem> items = Lists.newLinkedList();
	
	public Collection<EventStreamItem> getItems()
	{
		return this.items;
	}
}
