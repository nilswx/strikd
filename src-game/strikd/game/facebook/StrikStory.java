package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;
import strikd.facebook.FacebookStory;

public abstract class StrikStory extends FacebookStory
{
	// Generates Open Graph data for the given parameters (to be consumed by Facebook server)
	private static final String OBJECT_URL_FORMAT = "http://strik.it/og/object.php?type=%s&data=%s";
	
	private final Object objectParam;
	
	public StrikStory(Object objectParam, FacebookIdentity identity)
	{
		super(identity);
		this.objectParam = null;
	}

	@Override
	protected String getObjectUrl()
	{
		return String.format(OBJECT_URL_FORMAT, this.getObjectType(), String.valueOf(this.objectParam));
	}
}
