package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.ItemsMessage;
import strikd.communication.outgoing.PlayerInfoMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class LoginHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.LOGIN;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Not logged in already?
		if(!session.isLoggedIn())
		{
			// Read login info
			ObjectId playerId = new ObjectId(request.readStr());
			String token = request.readStr();
			String hardware = request.readStr();
			String systemVersion = request.readStr();
			
			// Correct account and password?
			Player player = session.getServer().getPlayerRegister().findPlayer(playerId);
			if(player == null || !token.equals(player.token))
			{
				session.end("bad login");
			}
			else
			{
				// Login OK!
				session.setPlayer(player, String.format("%s @ %s", hardware, systemVersion));
				
				// Push player data
				session.send(new PlayerInfoMessage(player));
				session.send(new FacebookStatusMessage(player.isFacebookLinked(), player.liked));
				session.send(new CurrencyBalanceMessage(player.balance));
				session.send(new ItemsMessage(player.items));
			}
		}
	}
}
