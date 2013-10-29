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
	private final String appAccessToken;
	private final ExecutorService publisher;
	
	public FacebookPublisher(String appNamespace, String appAccessToken)
	{
		this.appNamespace = appNamespace;
		this.appAccessToken = appAccessToken;
		this.publisher = Executors.newCachedThreadPool(new NamedThreadFactory("Facebook Publisher #%d"));
		
		// Log namespace and a masked copy of the token
		logger.info(String.format("og:namespace='%s', access_token=%s",
				this.appNamespace,
				appAccessToken.substring(0, appAccessToken.indexOf('|') + 1) + "<SECRET>"));
	}
	
	public Facebook getFacebook(String accessToken)
	{
		return new FacebookTemplate(accessToken, this.appNamespace);
	}
	
	public void publish(FacebookStory story)
	{
		this.publisher.execute(story);
	}
	
	public String getAppAccessToken()
	{
		// This token is derived from app ID + app secret, and is used to authorize the server to post stories
		// https://graph.facebook.com/oauth/access_token?client_id=APP_ID&client_secret=APP_SECRET&grant_type=client_credentials
		return this.appAccessToken;
	}
}
