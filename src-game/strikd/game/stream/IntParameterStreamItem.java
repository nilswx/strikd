package strikd.game.stream;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public abstract class IntParameterStreamItem extends ActivityStreamItem
{
	@Column(name="param")
	private int parameter;

	public int getParameter()
	{
		return this.parameter;
	}

	public void setParameter(int parameter)
	{
		this.parameter = parameter;
	}
}
