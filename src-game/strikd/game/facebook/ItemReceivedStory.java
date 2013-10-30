package strikd.game.facebook;

import strikd.facebook.FacebookIdentity;

public class ItemReceivedStory extends StrikHostedObjectStory
{
	public ItemReceivedStory(FacebookIdentity identity, int itemId)
	{
		super(identity, itemId);
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
