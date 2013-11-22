package strikd.game.match;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import strikd.Server;
import strikd.cluster.ServerDescriptor;
import strikd.game.player.Player;

public class MatchJournal extends Server.Referent
{
	public MatchJournal(Server server)
	{
		super(server);
	}

	@Entity @Table(name="matches")
	public static class Entry
	{
		@Id
		private long id;

		@ManyToOne(optional=false)
		private ServerDescriptor server;
		
		@Column(nullable=false)
		private Date startTime;
		
		@Column(nullable=false)
		private Date endTime;
		
		@ManyToOne(optional=false)
		private Player playerOne;
		
		@ManyToOne(optional=false)
		private Player playerTwo;
		
		@ManyToOne(optional=true)
		private Player winner;

		/**
		 * Gets the id of this {@link enclosing_type}.
		 * @return the id
		 */
		public long getId()
		{
			return this.id;
		}

		/**
		 * Modifies the id of this MatchJournal.Entry.
		 * @param id the id to set
		 */
		public void setId(long id)
		{
			this.id = id;
		}

		/**
		 * Gets the server of this {@link enclosing_type}.
		 * @return the server
		 */
		public ServerDescriptor getServer()
		{
			return this.server;
		}

		/**
		 * Modifies the server of this MatchJournal.Entry.
		 * @param server the server to set
		 */
		public void setServer(ServerDescriptor server)
		{
			this.server = server;
		}

		/**
		 * Gets the startTime of this {@link enclosing_type}.
		 * @return the startTime
		 */
		public Date getStartTime()
		{
			return this.startTime;
		}

		/**
		 * Modifies the startTime of this MatchJournal.Entry.
		 * @param startTime the startTime to set
		 */
		public void setStartTime(Date startTime)
		{
			this.startTime = startTime;
		}

		/**
		 * Gets the endTime of this {@link enclosing_type}.
		 * @return the endTime
		 */
		public Date getEndTime()
		{
			return this.endTime;
		}

		/**
		 * Modifies the endTime of this MatchJournal.Entry.
		 * @param endTime the endTime to set
		 */
		public void setEndTime(Date endTime)
		{
			this.endTime = endTime;
		}

		/**
		 * Gets the playerOne of this {@link enclosing_type}.
		 * @return the playerOne
		 */
		public Player getPlayerOne()
		{
			return this.playerOne;
		}

		/**
		 * Modifies the playerOne of this MatchJournal.Entry.
		 * @param playerOne the playerOne to set
		 */
		public void setPlayerOne(Player playerOne)
		{
			this.playerOne = playerOne;
		}

		/**
		 * Gets the playerTwo of this {@link enclosing_type}.
		 * @return the playerTwo
		 */
		public Player getPlayerTwo()
		{
			return this.playerTwo;
		}

		/**
		 * Modifies the playerTwo of this MatchJournal.Entry.
		 * @param playerTwo the playerTwo to set
		 */
		public void setPlayerTwo(Player playerTwo)
		{
			this.playerTwo = playerTwo;
		}

		/**
		 * Gets the winner of this {@link enclosing_type}.
		 * @return the winner
		 */
		public Player getWinner()
		{
			return this.winner;
		}

		/**
		 * Modifies the winner of this MatchJournal.Entry.
		 * @param winner the winner to set
		 */
		public void setWinner(Player winner)
		{
			this.winner = winner;
		}
	}
}
