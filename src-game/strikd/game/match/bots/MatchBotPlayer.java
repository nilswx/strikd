package strikd.game.match.bots;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.communication.outgoing.MatchEndedMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.board.Tile;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.ai.SimpleMatchBotAI;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;
import strikd.util.NamedThreadFactory;

public class MatchBotPlayer extends MatchPlayer implements Runnable
{
	private static final ScheduledExecutorService sharedAiExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AI Processor"));
	
	private final Player bot;
	private final MatchBotAI ai;
	private ScheduledFuture<?> pendingAiTask;
	
	public MatchBotPlayer(Player bot)
	{
		// Setup player
		super(null);
		this.bot = bot;
		
		// Install default AI implementation
		this.ai = new SimpleMatchBotAI(this);
		
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
		List<Tile> tiles = this.ai.nextMove();
		if(tiles != null && tiles.size() > 0)
		{
			// Select these tiles
			for(Tile tile : tiles)
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
		this.handleEvents(msg);
	}
	
	@Override
	public void sendDuplicate(OutgoingMessage msg)
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
			this.cancelAI();
		}	
	}
	
	@Override
	public Player getInfo()
	{
		return this.bot;
	}
}
