package strikd.facebook;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

@Embeddable
public class FacebookIdentity
{
	@Column(name="fb_uid")
	private long userId;
	@Column(name="fb_token")
	private String token;
	@Column(name="fb_name")
	private String name;
	
	@Transient
	private Facebook api;
	
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Facebook getAPI()
	{
		if(this.api == null)
		{
			// No reference to Server's instance available
			this.api = new FacebookTemplate(this.token, FacebookManager.getSharedAppNamespace());
		}
		return this.api;
	}
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
