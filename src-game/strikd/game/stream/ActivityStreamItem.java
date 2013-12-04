package strikd.game.stream;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import strikd.game.player.Player;

@Entity @Table(name="stream")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.CHAR, length=1)
public abstract class ActivityStreamItem
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne(optional=false)
	private Player player;
	
	@Column(nullable=false)
	private Date timestamp;
	
	public long getId()
	{
		return this.id;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}
}
