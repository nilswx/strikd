package strikd.game.user;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

public class FacebookIdentity
{
	public long userId;
	public String token;
	
	public Facebook getAPI()
	{
		return new FacebookTemplate(this.token);
	}
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
