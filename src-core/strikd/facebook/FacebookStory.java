package strikd.facebook;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public abstract class FacebookStory implements Runnable
{
	private static final Logger logger = Logger.getLogger(FacebookStory.class);
	
	private final FacebookIdentity identity;
	
	public FacebookStory(FacebookIdentity identity)
	{
		this.identity = identity;
	}
	
	@Override
	public void run()
	{
		try
		{
			// Retrieve Facebook API
			Facebook facebook = this.identity.getAPI();
			
			// Build data for the request (access_token = app access token)
			MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();
			data.set(this.getObjectType(), this.getObjectUrl());
			data.set("access_token", this.getPublisherAccessToken());
			
			// Make the request
			facebook.publish(Long.toString(this.identity.userId), (facebook.getApplicationNamespace() + ':' + this.getAction()), data);
		}
		catch(Exception e)
		{
			logger.warn(String.format("error publishing a new %s story for #%d", this.getAction(), this.identity.userId), e);
		}
	}
	
	protected abstract String getAction();
	
	protected abstract String getObjectType();
	
	protected abstract String getObjectUrl();
	
	protected String getPublisherAccessToken()
	{
		// No reference to Server's instance available
		return FacebookManager.getSharedAppAccessToken();
	}
}
