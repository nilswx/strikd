package strikd.facebook;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;

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
			this.identity.openGraphOperations().publishAction(this.getAction(), this.getObjectType(), this.getObjectUrl());
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
