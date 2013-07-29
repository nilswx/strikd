package strikd.communication.incoming;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

public final class MessageHandlers
{
	private static final Logger logger = Logger.getLogger(MessageHandlers.class);
	private static final HashMap<String, MessageHandler> handlers = new HashMap<String, MessageHandler>();
	
	private MessageHandlers()
	{
		
	}
	
	public static final MessageHandler get(String opcode)
	{
		return handlers.get(opcode);
	}
	
	private static final void register(Class<? extends MessageHandler> clazz)
	{
		try
		{
			MessageHandler handler = clazz.newInstance();
			handlers.put(handler.getOpcode(), handler);
			logger.debug(String.format("\"%s\" > %s", handler.getOpcode(), clazz.getName()));
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			logger.error("cannot register?!", e);
		}
	}
	
	static
	{
		Reflections ref = new Reflections("strikd.communication.incoming");
		for(Class<? extends MessageHandler> clazz : ref.getSubTypesOf(MessageHandler.class))
		{
			register(clazz);
		}
	}
}
