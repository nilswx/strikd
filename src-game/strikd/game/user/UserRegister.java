package strikd.game.user;

import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.ServerInstance;
import strikd.sessions.Session;

public class UserRegister
{
	private static final Logger logger = Logger.getLogger(UserRegister.class);
	
	private final ServerInstance instance;
	private final MongoCollection dbUsers;
	
	public UserRegister(ServerInstance instance)
	{
		this.instance = instance;
		this.dbUsers = instance.getDbCluster().getCollection("users");
	}
	
	public User newUser()
	{
		// Create new user with default data
		User user = new User();
		user.token = UUID.randomUUID().toString();
		user.name = this.generateGuestName();
		this.dbUsers.save(user);
		
		logger.debug(String.format("created user %s", user));
		
		return user;
	}
	
	public void saveUser(User user)
	{
		this.dbUsers.save(user);
	}
	
	public User getUser(ObjectId userId)
	{
		Session session = this.instance.getSessionMgr().getUserSession(userId);
		if(session != null)
		{
			return session.getUser();
		}
		else
		{
			return this.dbUsers.findOne(userId).as(User.class);
		}
	}
	
	public String generateGuestName()
	{
		Random rand = new Random();
		
		return "guest" + (rand.nextInt(9999 - 100 + 1) + 100);
	}
}
