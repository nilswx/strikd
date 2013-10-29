package strikd.game.facebook;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import strikd.Server;
import strikd.game.user.User;
import strikd.game.user.UserRegister;

public class InviteManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(InviteManager.class);
	
	private final MongoCollection inviteStore;
	
	public InviteManager(Server server)
	{
		super(server);
		this.inviteStore = server.getDbCluster().getCollection("invites");
		
		logger.info(String.format("pending invites for %d persons", this.inviteStore.count()));
	}
	
	private InvitedByList getList(long personId)
	{
		return this.inviteStore.findOne("{_id:#}", personId).as(InvitedByList.class);
	}
	
	public boolean registerInvite(long personId, User inviter)
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
		else if(list.userIds.contains(inviter.id))
		{
			return false;
		}
		
		// Done!
		list.userIds.add(inviter.id);
		logger.info(String.format("%s invited person #%d!", inviter, personId));
		
		// Save list
		this.inviteStore.save(list);
		return true;
	}
	
	public void processInvites(long personId)
	{
		// Was this FB user invited by existing players?
		InvitedByList list = this.getList(personId);
		if(list != null)
		{
			// Give rewards for these users
			UserRegister userRegister = this.getServer().getUserRegister();
			for(ObjectId userId : list.userIds)
			{
				// User _should_ still exist...
				User user = userRegister.findUser(userId);
				if(user != null)
				{
					logger.info(String.format("FB user #%d linked, rewarding %s for invite", personId, user));
				}
			}
			
			// Destroy invite list
			this.inviteStore.remove("{_id:#}", list.personId);
		}
	}
	
	private static class InvitedByList
	{
		@Id
		public long personId;
		
		@JsonProperty("by")
		public List<ObjectId> userIds;
		
		public InvitedByList(long personId)
		{
			this.personId = personId;
			this.userIds = Lists.newArrayListWithExpectedSize(1);
		}
	}
}
