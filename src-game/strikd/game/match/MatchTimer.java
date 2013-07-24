package strikd.game.match;

public class MatchTimer
{
	private int timeLeft;
	
	public MatchTimer(int seconds)
	{
		this.timeLeft = seconds;
	}
	
	public int getTimeLeft()
	{
		return this.timeLeft;
	}
	
	public boolean isDone()
	{
		return (this.timeLeft == 0);
	}
	
	public void tick()
	{
		if(this.isDone())
		{
			
		}
	}
}
