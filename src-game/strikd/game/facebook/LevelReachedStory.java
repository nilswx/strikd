package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class LevelReachedStory extends StrikStory
{
	public LevelReachedStory(FacebookIdentity identity, int level)
	{
		super(identity, level);
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
