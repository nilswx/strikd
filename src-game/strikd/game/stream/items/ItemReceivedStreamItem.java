package strikd.game.stream.items;

import com.fasterxml.jackson.annotation.JsonProperty;

import strikd.game.items.ItemType;

public class ItemReceivedStreamItem extends PlayerStreamItem
{
	@JsonProperty("i")
	public ItemType item;
}
