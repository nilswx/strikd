package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
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
		// Determine the criteria
		ObjectId playerId = new ObjectId(request.readStr());
		long begin = request.readLong();
		long end = request.readLong();
	}
}
