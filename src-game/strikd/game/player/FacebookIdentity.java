package strikd.game.player;

public class FacebookIdentity
{
	public long userId;
	public String authToken;
	public String authSecret;
	public String country;
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
