package strikd.facebook;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class FacebookStory implements Runnable
{
	private static final Logger logger = Logger.getLogger(FacebookStory.class);
	
	private final Facebook identity;
	
	public FacebookStory(Facebook identity)
	{
		this.identity = identity;
	}
	
	@Override
	public void run()
	{
		try
		{
			// Build data for the request (access_token = server's)
			MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
			data.set(this.getObjectType(), this.getObjectUrl());
			data.set("access_token", "");
			
			// Make the request
			this.identity.publish("me", (this.identity.getApplicationNamespace() + ':' + this.getAction()), data);
		}
		catch(Exception e)
		{
			logger.warn(String.format("error publishing a new %s story", this.getAction()), e);
		}
	}
	
	protected abstract String getAction();
	
	protected abstract String getObjectType();
	
	protected abstract String getObjectUrl();
}
