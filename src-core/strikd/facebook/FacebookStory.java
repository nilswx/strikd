package strikd.facebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
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
	
	protected abstract String getAction();

	protected abstract String getObjectType();

	protected Object getObject()
	{
		return this.object;
	}

	@Override
	public void run()
	{
		try
		{
			// TODO: cache this flag until we run into an exception, or enough time has elapsed?
			boolean isPermissionGranted = this.checkPublishActionPermission();
			if(isPermissionGranted)
			{
				this.doPublish();
			}
		}
		catch(Exception e)
		{
			logger.warn("error handling {}", this, e);
		}
	}
	
	private boolean checkPublishActionPermission()
	{
		// Log what we're doing
		logger.info("checking 'publish_stream' permission for {}", this.identity.getUserId());
		
		try
		{
			// Fetch user permissions
			FacebookClient api = this.identity.getAPI();
			JsonArray data = api.fetchObject("me/permissions", JsonObject.class).optJsonArray("data");
			JsonObject permissions = (data != null) ? data.optJsonObject(0) : null;
			
			// Evaluate permissions
			return (permissions != null && permissions.has("publish_stream"));
		}
		catch(Exception e)
		{
			logger.warn("could not check 'publish_stream' permission for {}", this.identity.getUserId());
			return false;
		}
	}
	
	private void doPublish()
	{
		try
		{
			// Retrieve Facebook API instance (with app token)
			FacebookClient app = FacebookManager.getSharedAppAPI();
			
			// Retrieve namespace
			String namespace = FacebookManager.getSharedAppNamespace();
			
			// Make the request
			logger.info("publishing a new {}", this);
			app.publish(String.format("%d/%s:%s", this.identity.getUserId(), namespace, this.getAction()), JsonObject.class,
					Parameter.with(this.getObjectType(), this.getObject()));
		}
		catch(Exception e)
		{
			logger.warn("could not publish {}", this, e);
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("%s [uid=%d]", this.getClass().getSimpleName(), this.identity.getUserId());
	}
}
