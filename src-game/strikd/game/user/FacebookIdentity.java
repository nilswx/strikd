package strikd.game.user;

public class FacebookIdentity
{
	public long userId;
	public String token;
	
	@Override
	public String toString()
	{
		return "FB UID #" + this.userId;
	}
}
