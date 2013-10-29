package strikd.game.facebook;

import org.springframework.social.facebook.api.Facebook;

public class ProfileBeatedStory extends StrikStory
{
	public ProfileBeatedStory(String profileId, Facebook identity)
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
