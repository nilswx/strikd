package strikd.facebook;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public abstract class FacebookStory implements Runnable
{
	private static final Logger logger = Logger.getLogger(FacebookStory.class);
	
	private final FacebookIdentity identity;
	private final Object object;
	
	public FacebookStory(FacebookIdentity identity, Object object)
	{
		this.identity = identity;
		this.object = object;
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
			data.set(this.getObjectType(), this.getObjectString());
			data.set("access_token", this.getPublisherAccessToken());
			
			// Make the request
			logger.debug(String.format("publishing a new %s", this));
			facebook.publish(Long.toString(this.identity.userId), (facebook.getApplicationNamespace() + ':' + this.getAction()), data);
		}
		catch(Exception e)
		{
			logger.warn(String.format("error publishing a new %s", this), e);
		}
	}
	
	protected abstract String getAction();
	
	protected abstract String getObjectType();
	
	protected final Object getObject()
	{
		return this.object;
	}
	
	protected String getObjectString()
	{
		return String.valueOf(this.object);
	}
	
	protected String getPublisherAccessToken()
	{
		// No reference to Server's instance available
		return FacebookManager.getSharedAppAccessToken();
	}
	
	@Override
	public String toString()
	{
		return String.format("%s [uid=%d]", this.getClass().getSimpleName(), this.identity.userId);
	}
}
