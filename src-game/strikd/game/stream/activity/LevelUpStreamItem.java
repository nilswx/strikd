package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import strikd.game.stream.IntParameterStreamItem;

@Entity @DiscriminatorValue(LevelUpStreamItem.TYPE)
public class LevelUpStreamItem extends IntParameterStreamItem
{
	public int getLevel()
	{
		return super.getParameter();
	}

	public void setLevel(int level)
	{
		super.setParameter(level);
	}
	
	public static final String TYPE = "l";
}
