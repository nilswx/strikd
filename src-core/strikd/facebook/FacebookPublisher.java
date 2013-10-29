package strikd.facebook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import strikd.util.NamedThreadFactory;

public class FacebookPublisher
{
	private static final Logger logger = Logger.getLogger(FacebookPublisher.class);
	
	private final String appNamespace;
	private final String appSecret;
	private final ExecutorService publisher;
	
	public FacebookPublisher(String appNamespace, String appSecret)
	{
		this.appNamespace = appNamespace;
		this.appSecret = appSecret;
		this.publisher = Executors.newCachedThreadPool(new NamedThreadFactory("Facebook Publisher #%d"));
		logger.info(String.format("og:namespace='%s', secret=<HIDDEN>", this.appNamespace));
	}
	
	public Facebook getFacebook(String accessToken)
	{
		return new FacebookTemplate(accessToken, this.appNamespace);
	}
	
	public void publish(FacebookStory story)
	{
		this.publisher.execute(story);
	}
}
