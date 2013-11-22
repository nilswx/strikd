package strikd.communication.incoming;

import java.util.List;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.ActivityStreamMessage;
import strikd.game.player.Player;
import strikd.game.stream.ActivityStreamItem;
import strikd.game.stream.ActivityStreamManager;
import strikd.net.codec.IncomingMessage;

public class GetActivityStreamHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_ACTIVITY_STREAM;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Stream available?
		ActivityStreamManager stream = session.getServer().getActivityStream();
		if(stream != null)
		{
			// Resolve player
			long playerId = request.readLong();
			Player player = session.getServer().getPlayerRegister().findPlayer(playerId);
			if(player != null)
			{
				// Determine period
				int begin = request.readInt();
				int end = request.readInt();
				
				// Send all items in this period
				List<ActivityStreamItem> items = stream.getPlayerStream(player, begin, end, session.getPlayer());
				session.send(new ActivityStreamMessage(begin, end, items));
			}
		}
	}
}
