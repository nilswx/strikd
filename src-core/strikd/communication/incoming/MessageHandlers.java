package strikd.communication.incoming;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;

public final class MessageHandlers
{
	private static final Logger logger = LoggerFactory.getLogger(MessageHandlers.class);
	private static final MessageHandler[] handlers = new MessageHandler[Opcodes.Outgoing.values().length];
	
	private MessageHandlers() { }
	
	public static void load()
	{
		// Triggers static constructor
	}
	
	public static final MessageHandler get(Opcodes.Incoming op)
	{
		return handlers[op.ordinal()];
	}
	
	private static final void register(Class<? extends MessageHandler> clazz)
	{
		try
		{
			MessageHandler handler = clazz.newInstance();
			handlers[handler.getOpcode().ordinal()] = handler;
			logger.debug("{} > {}", handler.getOpcode(), clazz.getName());
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
