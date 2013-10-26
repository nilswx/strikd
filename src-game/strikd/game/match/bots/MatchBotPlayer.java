package strikd.game.match.bots;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.communication.outgoing.MatchEndedMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.board.Square;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.ai.BasicMatchBotAI;
import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;

public class MatchBotPlayer extends MatchPlayer implements Runnable
{
	private static ScheduledExecutorService sharedAiExecutor = Executors.newSingleThreadScheduledExecutor();
	
	private final User bot;
	private final MatchBotAI ai;
	private ScheduledFuture<?> pendingAiTask;
	
	public MatchBotPlayer(User bot)
	{
		// Setup player
		super(null);
		this.bot = bot;
		
		// Install default AI implementation
		this.ai = new BasicMatchBotAI(this);
		
		// "Ready whenever you are"
		this.setReady();
	}
	
	public void scheduleNextMove()
	{
		if(this.pendingAiTask == null)
		{
			this.pendingAiTask = sharedAiExecutor.schedule(this, this.ai.getNextMoveDelay(), TimeUnit.MILLISECONDS);
		}
	}
	
	public void cancelAI()
	{
		this.pendingAiTask.cancel(false);
		this.pendingAiTask = null;
	}
	
	@Override
	public void run()
	{
		// New move available?
		List<Square> tiles = this.ai.nextMove();
		if(tiles != null && tiles.size() > 0)
		{
			// Select these tiles
			for(Square tile : tiles)
			{
				this.selectTile(tile);
			}
			
			// TODO: award points etc
		}
		
		// Next!
		this.pendingAiTask = null;
		this.scheduleNextMove();
	}
	
	
	@Override
	public void send(OutgoingMessage msg)
	{
		// Handle on special events
		if(msg instanceof MatchStartedMessage)
		{
			this.scheduleNextMove();
		}
		else if(msg instanceof MatchEndedMessage)
		{
			this.cancelAI();
		}
		
		// No backing session, send to -> /dev/null
	}
	
	@Override
	public User getInfo()
	{
		return this.bot;
	}
}
