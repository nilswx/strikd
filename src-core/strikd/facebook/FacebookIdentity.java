package strikd.facebook;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FacebookIdentity
{
	public long userId;
	public String token;
	
	@JsonIgnore
	private Facebook api;
	
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
