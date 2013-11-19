package strikd.communication.incoming;

import java.util.Date;
import java.util.List;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.EventStreamMessage;
import strikd.game.player.Player;
import strikd.game.stream.EventStreamItem;
import strikd.game.stream.EventStreamManager;
import strikd.net.codec.IncomingMessage;

public class GetEventStreamPeriodHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_EVENT_STREAM_PERIOD;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Stream available?
		EventStreamManager stream = session.getServer().getEventStreamMgr();
		if(stream != null)
		{
			// Resolve player
			long playerId = request.readLong();
			Player player = session.getServer().getPlayerRegister().findPlayer(playerId);
			if(player != null)
			{
				// Determine period
				Date begin = new Date(request.readLong());
				Date end = new Date(request.readLong());
				
				// Send all items in this period
				List<EventStreamItem> items = stream.getPlayerStream(player, begin, end, session.getPlayer());
				session.send(new EventStreamMessage(begin, end, items));
			}
		}
	}
}
