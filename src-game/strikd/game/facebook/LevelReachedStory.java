package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class LevelReachedStory extends StrikStory
{
	public LevelReachedStory(int level, FacebookIdentity identity)
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
