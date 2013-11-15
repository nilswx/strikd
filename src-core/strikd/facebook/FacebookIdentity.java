package strikd.facebook;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class FacebookIdentity
{
	public long userId;
	public String token;
	
	public String realName;
	public List<ObjectId> friends;
	
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
