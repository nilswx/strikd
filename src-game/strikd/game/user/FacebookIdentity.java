package strikd.game.user;

public class FacebookIdentity
{
	public long userId;
	public String authToken;
	public String authSecret;
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
