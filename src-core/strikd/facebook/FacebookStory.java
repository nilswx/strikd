package strikd.facebook;

import org.springframework.social.facebook.api.Facebook;

public class FacebookStory implements Runnable
{
	private final Facebook identity;
	
	public FacebookStory(Facebook identity)
	{
		this.identity = identity;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		
	}
}
