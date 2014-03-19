package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;
import strikd.facebook.FacebookStory;

public abstract class StrikHostedObjectStory extends FacebookStory
{
	public StrikHostedObjectStory(FacebookIdentity identity, Object object)
	{
		super(identity, object);
	}

	// Generates Open Graph data for the given parameters (to be consumed by Facebook server)
	private static final String OBJECT_URL_FORMAT = "http://strik.it/facebook/object.php?type=%s&data=%s";
	
	@Override
	protected String getObject()
	{
		// The object is self-hosted, at a URL!
		return String.format(OBJECT_URL_FORMAT, this.getObjectType(), super.getObject());
	}
}
