package strikd.game.achievements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import strikd.game.player.Player;

@Entity @Table(name="player_achievements")
public class AchievementProgress
{
	@ManyToOne(optional=false)
	private Player player;
	
	@ManyToOne(optional=false)
	private Achievement achievement;
	
	@Column(nullable=false)
	private int value;
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Achievement getAchievement()
	{
		return this.achievement;
	}
	
	public void setAchievement(Achievement achievement)
	{
		this.achievement = achievement;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
}
