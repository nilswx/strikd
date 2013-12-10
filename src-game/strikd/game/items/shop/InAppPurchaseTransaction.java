package strikd.game.items.shop;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import strikd.game.player.Player;

@Entity @Table(name="player_transactions")
public class InAppPurchaseTransaction
{
	@Id
	private long id;
	
	@ManyToOne(optional=true)
	private Player player;
	
	@Version
	private Date timestamp;
	
	public InAppPurchaseTransaction(long id, Player player)
	{
		this.id = id;
		this.player = player;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}
}
