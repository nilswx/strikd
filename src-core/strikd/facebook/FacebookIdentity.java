package strikd.facebook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

@Entity @Table(name="facebook")
public class FacebookIdentity
{
	@Id
	private long userId;
	
	@Column(nullable=false)
	private String token;
	
	@Transient
	private FacebookClient api;
	
	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}
	
	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public FacebookClient getAPI()
	{
		if(this.api == null)
		{
			this.api = new DefaultFacebookClient(this.token);
		}
		return this.api;
	}
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
