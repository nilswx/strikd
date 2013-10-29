package strikd.game.facebook;

import org.springframework.social.facebook.api.Facebook;

public class ItemReceivedStory extends StrikStory
{
	public ItemReceivedStory(int itemId, Facebook identity)
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
