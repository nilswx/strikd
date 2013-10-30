package strikd.game.facebook;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import strikd.Server;
import strikd.game.player.Player;
import strikd.game.player.PlayerRegister;

public class FacebookInviteManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(FacebookInviteManager.class);
	
	private final MongoCollection dbInvites;
	
	public FacebookInviteManager(Server server)
	{
		super(server);
		this.dbInvites = server.getDbCluster().getCollection("invites");
		
		logger.info(String.format("pending invites for %d persons", this.dbInvites.count()));
	}
	
	private InvitedByList getList(long personId)
	{
		return this.dbInvites.findOne("{_id:#}", personId).as(InvitedByList.class);
	}
	
	public boolean registerInvite(long personId, Player inviter)
	{
		// Inviter is FB linked?
		if(!inviter.isFacebookLinked())
		{
			return false;
		}
		
		// Get (new) list
		InvitedByList list = this.getList(personId);
		if(list == null)
		{
			list = new InvitedByList(personId);
		}
		else if(list.playerIds.contains(inviter.id))
		{
			return false;
		}
		
		// Done!
		list.playerIds.add(inviter.id);
		logger.info(String.format("%s invited person #%d!", inviter, personId));
		
		// Save list
		this.dbInvites.save(list);
		return true;
	}
	
	public void processInvites(long personId)
	{
		// Was this FB user invited by existing players?
		InvitedByList list = this.getList(personId);
		if(list != null)
		{
			// Give rewards for these player
			PlayerRegister playerRegister = this.getServer().getPlayerRegister();
			for(ObjectId playerId : list.playerIds)
			{
				// Player _should_ still exist...
				Player player = playerRegister.findPlayer(playerId);
				if(player != null)
				{
					logger.info(String.format("FB user #%d linked, rewarding %s for invite", personId, player));
				}
			}
			
			// Destroy invite list
			this.dbInvites.remove("{_id:#}", list.personId);
		}
	}
	
	private static class InvitedByList
	{
		@Id
		public long personId;
		
		@JsonProperty("by")
		public List<ObjectId> playerIds;
		
		public InvitedByList(long personId)
		{
			this.personId = personId;
			this.playerIds = Lists.newArrayListWithExpectedSize(1);
		}
	}
}
