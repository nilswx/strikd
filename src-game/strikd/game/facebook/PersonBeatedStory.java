package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class PersonBeatedStory extends StrikStory
{
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
	
	@Override
	protected String getObjectString()
	{
		return String.valueOf(this.getObject());
	}
}
