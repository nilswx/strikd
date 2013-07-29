package strikd.game.user;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.Server;
import strikd.game.items.Item;
import strikd.sessions.Session;

public class UserRegister extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(UserRegister.class);
	
	private final MongoCollection dbUsers;
	
	public UserRegister(Server server)
	{
		super(server);
		this.dbUsers = this.getServer().getDbCluster().getCollection("users");
	}
	
	public User newUser()
	{
		// Create new user with default data
		User user = new User();
		user.token = UUID.randomUUID().toString();
		user.name = this.generateGuestName();
		
		Item x = new Item();
		x.typeId = 5;
		x.timestamp = new Date();
		x.data = "adasdf";
		user.items.add(x);
		
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
		Session session = this.getServer().getSessionMgr().getUserSession(userId);
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
