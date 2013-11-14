package strikd.game.stream.items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LevelUpStreamItem extends PlayerStreamItem
{
	@JsonProperty("l")
	public int level;
}
