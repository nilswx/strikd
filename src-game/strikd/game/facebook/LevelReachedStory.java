package strikd.game.facebook;

import org.springframework.social.facebook.api.Facebook;

public class LevelReachedStory extends StrikStory
{
	public LevelReachedStory(int level, Facebook identity)
	{
		super(level, identity);
	}

	@Override
	protected String getAction()
	{
		return "reach";
	}

	@Override
	protected String getObjectType()
	{
		return "level";
	}
}
