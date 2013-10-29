package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class ProfileBeatedStory extends StrikStory
{
	public ProfileBeatedStory(String profileId, FacebookIdentity identity)
	{
		super(profileId, identity);
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
