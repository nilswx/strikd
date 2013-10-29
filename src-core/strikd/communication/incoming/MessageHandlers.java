package strikd.communication.incoming;

import java.util.EnumMap;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import strikd.communication.Opcodes;

public final class MessageHandlers
{
	private static final Logger logger = Logger.getLogger(MessageHandlers.class);
	private static final EnumMap<Opcodes.Incoming, MessageHandler> handlers = new EnumMap<Opcodes.Incoming, MessageHandler>(Opcodes.Incoming.class);
	
	private MessageHandlers()
	{
		
	}
	
	public static final MessageHandler get(Opcodes.Incoming op)
	{
		return handlers.get(op);
	}
	
	private static final void register(Class<? extends MessageHandler> clazz)
	{
		try
		{
			MessageHandler handler = clazz.newInstance();
			handlers.put(handler.getOpcode(), handler);
			logger.debug(String.format("%s > %s", handler.getOpcode(), clazz.getName()));
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			logger.error("cannot register?!", e);
		}
	}
	
	static
	{
		Reflections ref = new Reflections(MessageHandler.class.getPackage().getName());
		for(Class<? extends MessageHandler> clazz : ref.getSubTypesOf(MessageHandler.class))
		{
			register(clazz);
		}
	}
}
