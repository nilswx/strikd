package strikd.game.match.bots;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.communication.outgoing.MatchEndedMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.match.MatchPlayer;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;
import strikd.util.NamedThreadFactory;

public abstract class MatchBotPlayer extends MatchPlayer
{
	private static final ScheduledExecutorService sharedAiExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AI Processor"));
	
	private final Player bot;
	private final Runnable aiLoop;
	
	private ScheduledFuture<?> pendingAiTask;
	
	public MatchBotPlayer(Player bot)
	{
		// Setup player
		super(null);
		this.bot = bot;
		
		// Construct AI loop
		this.aiLoop = new Runnable()
		{
			@Override
			public void run()
			{
				// Go go!
				nextMove();
				
				// Next!
				scheduleNextMove();
			}
		};
		
		// "Ready whenever you are"
		this.setReady();
	}
	
	protected abstract boolean initializeAI();
	
	protected abstract int nextMoveDelay();
	
	protected abstract void nextMove();
	
	public void startAI()
	{
		if(this.initializeAI())
		{
			this.scheduleNextMove();
		}
	}
	
	public void stopAI()
	{
		if(this.pendingAiTask != null)
		{
			this.pendingAiTask.cancel(false);
			this.pendingAiTask = null;
		}
	}

	public void scheduleNextMove()
	{
		if(this.pendingAiTask == null)
		{
			int delay = this.nextMoveDelay();
			if(delay > 0)
			{
				this.pendingAiTask = sharedAiExecutor.schedule(this.aiLoop, delay, TimeUnit.MILLISECONDS);
			}
		}
		else
		{
			this.stopAI();
			this.scheduleNextMove();
		}
	}
	
	@Override
	public void send(OutgoingMessage msg)
	{
		this.handleEvents(msg);
	}
	
	private void handleEvents(OutgoingMessage msg)
	{
		if(msg instanceof MatchStartedMessage)
		{
			this.startAI();
		}
		else if(msg instanceof MatchEndedMessage)
		{
			this.stopAI();
		}	
	}
	
	@Override
	public final Player getInfo()
	{
		return this.bot;
	}
}
