package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @DiscriminatorValue(LevelUpStreamItem.TYPE)
public class LevelUpStreamItem extends ActivityStreamItem
{
	private int level;

	public int getLevel()
	{
		return this.level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public static final String TYPE = "l";
}
