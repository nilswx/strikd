package strikd.game.facebook;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import org.jongo.MongoCollection;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	public void registerInvite(long personId, Player inviter)
	{
		this.dbInvites.update("{_id:#}", personId).upsert().with("{$addToSet:{by:#}}", inviter.id);	
		logger.info(String.format("%s invited person #%d!", inviter, personId));
	}
	
	public void processInvites(long personId)
	{
		// Was this FB user invited by existing players?
		InvitedByList invites = this.dbInvites.findAndModify("{_id:#}", personId).remove().as(InvitedByList.class);
		if(invites != null)
		{
			// Reward these guys
			PlayerRegister register = this.getServer().getPlayerRegister();
			for(ObjectId playerId : invites.playerIds)
			{
				// Player _should_ still exist
				Player player = register.findPlayer(playerId);
				if(player != null)
				{
					logger.info(String.format("FB user #%d linked, rewarding %s for invite", personId, player));
				}
			}
		}
	}
	
	/* This POJO is used because Jongo doesn't properly deserialize a List of ObjectId's directly */
	private static class InvitedByList
	{
		@JsonProperty("by")
		public List<ObjectId> playerIds;
	}
}
