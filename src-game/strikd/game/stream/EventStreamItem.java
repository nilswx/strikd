package strikd.game.stream;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.CLASS,property="_c")
public abstract class EventStreamItem
{
	@JsonProperty("t")
	public Date timestamp;
}
