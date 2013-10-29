package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class ItemReceivedStory extends StrikStory
{
	public ItemReceivedStory(int itemId, FacebookIdentity identity)
	{
		super(itemId, identity);
	}

	@Override
	protected String getAction()
	{
		return "receive";
	}

	@Override
	protected String getObjectType()
	{
		return "item";
	}
}
