package strikd.game.match;

import java.util.ArrayList;
import java.util.List;

import strikd.communication.outgoing.TileSelectionClearedMessage;
import strikd.game.board.Tile;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;
import strikd.sessions.Session;

public class MatchPlayer
{
	private final Session session;
	
	private Match match;
	private int playerId;
	private boolean ready;
	private int score;
	
	private List<Tile> selected = new ArrayList<Tile>();
	
	public MatchPlayer(Session session)
	{
		this.session = session;
	}
	
	public void send(OutgoingMessage msg)
	{
		this.session.send(msg);
	}
	
	public MatchPlayer setMatch(Match match, int playerId)
	{
		this.match = match;
		this.playerId = playerId;
		if(this.session != null)
		{
			this.session.setMatchPlayer(this);
		}
		
		return this;
	}
	
	public void leave()
	{
		this.match.removePlayer(this);
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	public Player getInfo()
	{
		return this.session.getPlayer();
	}
	
	public Match getMatch()
	{
		return this.match;
	}
	
	public int getPlayerId()
	{
		return this.playerId;
	}
	
	public void setReady()
	{
		this.ready = true;
		if(this.match != null)
		{
			this.match.checkReady();
		}
	}
	
	public boolean isReady()
	{
		return this.ready;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void modScore(int points)
	{
		this.score += points;
	}
	
	public void selectTile(Tile tile)
	{
		this.selected.add(tile);
		tile.onSelect(this);
	}
	
	public List<Tile> getSelection()
	{
		return this.selected;
	}
	
	public void clearSelection()
	{
		// Deselect all tiles
		for(Tile tile : this.selected)
		{
			tile.onDeselect(this);
		}
		this.selected.clear();
		
		// Notify match
		this.match.broadcast(new TileSelectionClearedMessage(this));
	}
	
	public MatchPlayer getOpponent()
	{
		return this.match.getOpponent(this);
	}
	
	@Override
	public String toString()
	{
		return this.getInfo().getName();
	}
}
