package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.game.news.NewsManager;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class GetNewsHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_NEWS;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		NewsManager news = session.getServer().getNewsMgr();
		session.sendCopy(news.getCachedNewsMessage());
	}
}
