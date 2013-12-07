package strikd.game.match.bots;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.communication.outgoing.MatchEndedMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.ai.DefaultMatchBotAI;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;
import strikd.util.NamedThreadFactory;

public class MatchBotPlayer extends MatchPlayer
{
	private static final ScheduledExecutorService sharedAiExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AI Processor"));
	
	private final Player bot;
	private final MatchBotAI ai;
	private ScheduledFuture<?> pendingAiTask;
	private final Runnable aiRunnable;
	
	public MatchBotPlayer(Player bot)
	{
		// Setup player
		super(null);
		this.bot = bot;
		
		// Install default AI implementation
		this.ai = new DefaultMatchBotAI(this);
		this.aiRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				// Go go!
				ai.nextMove();
				
				// Next!
				scheduleNextMove();
			}
		};
		
		// "Ready whenever you are"
		this.setReady();
	}
	
	public boolean isIdle()
	{
		return (this.pendingAiTask == null);
	}
	
	public boolean isThinking()
	{
		return !this.isIdle();
	}
	
	public void stopAI()
	{
		if(this.isThinking())
		{
			this.pendingAiTask.cancel(false);
			this.pendingAiTask = null;
		}
	}
	
	public void scheduleNextMove()
	{
		if(this.isIdle())
		{
			this.pendingAiTask = sharedAiExecutor.schedule(this.aiRunnable, this.ai.nextMoveDelay(), TimeUnit.MILLISECONDS);
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
			this.scheduleNextMove();
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
