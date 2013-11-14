package strikd.game.stream;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class EventStreamItem
{
	@JsonProperty("t")
	public Date timestamp;
}
