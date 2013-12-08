package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;
import strikd.facebook.FacebookStory;

public class PersonBeatedStory extends FacebookStory
{
	public PersonBeatedStory(FacebookIdentity identity, long userId)
	{
		super(identity, Long.toString(userId));
	}
	
	public PersonBeatedStory(FacebookIdentity identity, String profileId)
	{
		super(identity, profileId);
	}

	@Override
	protected String getAction()
	{
		return "beat";
	}

	@Override
	protected String getObjectType()
	{
		return "profile";
	}
}
