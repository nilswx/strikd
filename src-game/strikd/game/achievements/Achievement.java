package strikd.game.achievements;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="achievements")
public class Achievement
{
	@Id
	private final int id;
	
	@Column(nullable=false)
	private final String code;
	
	public Achievement(int id, String code)
	{
		this.id = id;
		this.code = code;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getCode()
	{
		return this.code;
	}
}
