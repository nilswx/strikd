package strikd.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;

public abstract class FacebookStory implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(FacebookStory.class);

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
			// Retrieve Facebook API with app token (pooling?)
			FacebookClient api = new DefaultFacebookClient(FacebookManager.getSharedAppAccessToken());

			// Make the request
			logger.debug("publishing a new {}", this);
			api.publish(String.format("%d/%s:%s", this.identity.getUserId(), FacebookManager.getSharedAppNamespace(), this.getAction()), JsonObject.class,
					Parameter.with(this.getObjectType(), this.getObject()));
		}
		catch(Exception e)
		{
			logger.warn("error publishing a new {}", this, e);
		}
	}

	protected abstract String getAction();

	protected abstract String getObjectType();

	protected Object getObject()
	{
		return this.object;
	}

	protected String getPublisherAccessToken()
	{
		// No reference to Server's instance available
		return FacebookManager.getSharedAppAccessToken();
	}

	@Override
	public String toString()
	{
		return String.format("%s [uid=%d]", this.getClass().getSimpleName(), this.identity.getUserId());
	}
}
