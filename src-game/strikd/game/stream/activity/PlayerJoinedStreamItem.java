package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @DiscriminatorValue(PlayerJoinedStreamItem.TYPE)
public class PlayerJoinedStreamItem extends ActivityStreamItem
{	
	public static final String TYPE = "j";
}
