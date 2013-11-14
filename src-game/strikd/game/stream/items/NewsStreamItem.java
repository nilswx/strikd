package strikd.game.stream.items;

import java.util.Date;

import strikd.game.stream.EventStreamItem;

public class NewsStreamItem extends EventStreamItem
{
	public Date published;
	public String imageUrl;
	public String headline;
	public String body;
}
