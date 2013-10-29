package strikd.game.facebook;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import strikd.facebook.FacebookIdentity;
import strikd.facebook.FacebookManager;
import strikd.game.user.User;
import strikd.game.user.UserRegister;

public class InviteManager
{
	private static final Logger logger = Logger.getLogger(InviteManager.class);
	
	private final FacebookManager facebookMgr;
	private final MongoCollection inviteStore;
	
	public InviteManager(FacebookManager facebookMgr)
	{
		this.facebookMgr = facebookMgr;
		this.inviteStore = facebookMgr.getServer().getDbCluster().getCollection("invites");
		
		logger.info(String.format("pending: %d persons", this.inviteStore.count()));
	}
	
	private InvitedByList getList(long personId)
	{
		return this.inviteStore.findOne("{_id:#}", personId).as(InvitedByList.class);
	}
	
	public boolean issueInvite(long personId, User inviter)
	{
		// Inviter is FB linked?
		if(inviter.isFacebookLinked())
		{
			return false;
		}
		
		// Get (new) list
		InvitedByList list = this.getList(personId);
		if(list == null)
		{
			list = new InvitedByList();
			list.personId = personId;
			list.userIds = Lists.newArrayListWithExpectedSize(1);
		}
		else
		{
			// Already invited by this user?
			if(list.userIds.contains(inviter.id))
			{
				return false;
			}
		}
		
		// TODO: send invite through Facebook
		this.facebookMgr.toString();
		logger.info(String.format("inviting person #%d on behalf of %s", personId, inviter));
		
		// Save list
		this.inviteStore.save(list);
		return true;
	}
	
	public void processInvites(FacebookIdentity fbIdentity)
	{
		// Was this FB user invited by existing players?
		InvitedByList list = this.getList(fbIdentity.userId);
		if(list != null)
		{
			// Give rewards for these users
			UserRegister userRegister = this.facebookMgr.getServer().getUserRegister();
			for(ObjectId userId : list.userIds)
			{
				User user = userRegister.findUser(userId);
				if(user != null)
				{
					logger.info(String.format("FB user #%d linked, rewarding %s for invite", fbIdentity.userId, user));
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
	}
}
