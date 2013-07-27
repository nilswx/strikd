package strikd.game.player;

public class FacebookIdentity
{
	private long userId;
	private String authToken;
	private String authSecret;
	private String country;
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
