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
	
	@Column(nullable=false)
	private final int value;
	
	public Achievement(int id, String code, int value)
	{
		this.id = id;
		this.code = code;
		this.value = value;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public int getValue()
	{
		return this.value;
	}
}
