package strikd.game.match;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.util.NamedThreadFactory;

public class MatchTimer
{
	private static final ScheduledExecutorService scheduler = 
			Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("MatchTimer"));

	private final Match match;
	private final int duration;
	
	private long startTime;
	private ScheduledFuture<?> timeout;
	
	public MatchTimer(Match match, int duration)
	{
		this.match = match;
		this.duration = duration;
	}
	
	public void start()
	{
		if(this.isRunning())
		{
			throw new IllegalStateException("already started");
		}
		
		this.startTime = System.currentTimeMillis();
		this.timeout = scheduler.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				match.timerEnded();
			}
		}, this.duration, TimeUnit.SECONDS);
	}
	
	public void stop()
	{
		if(!this.isRunning())
		{
			throw new IllegalStateException("not started");
		}
		
		this.timeout.cancel(false);
		this.timeout = null;
	}
	
	public boolean isRunning()
	{
		return (this.timeout != null);
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	public int getTimeLeft()
	{
		if(this.isRunning())
		{
			return (int)((this.startTime + TimeUnit.SECONDS.toMillis(this.duration)) - System.currentTimeMillis());
		}
		else
		{
			return this.duration;
		}
	}
}
