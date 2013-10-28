package strikd.facebook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import strikd.util.NamedThreadFactory;

public class FacebookPublisher
{
	private final String appSecret;
	private final ExecutorService publisher;
	
	public FacebookPublisher(String appSecret)
	{
		this.appSecret = appSecret;
		this.publisher = Executors.newCachedThreadPool(new NamedThreadFactory("Facebook Publisher #%d"));
	}
	
	public void publish(FacebookStory story)
	{
		this.publisher.execute(story);
	}
}
