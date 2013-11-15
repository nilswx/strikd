package strikd.net.codec;

import org.apache.log4j.Logger;

import strikd.communication.Opcodes;

public final class MessageAllocatorExpert
{
	private static final Logger logger = Logger.getLogger(MessageAllocatorExpert.class);
	private static final boolean doDebug = logger.isDebugEnabled();
	
	private static final int[] bestSizes = new int[Opcodes.Outgoing.values().length];
	
	public static final int getBestSize(final Opcodes.Outgoing op)
	{
		return bestSizes[op.ordinal()];
	}
	
	public static final void reportSize(final Opcodes.Outgoing op, final int size)
	{
		if(size > bestSizes[op.ordinal()])
		{
			if(doDebug)
			{
				logger.debug(String.format("%s: %d -> %d bytes", op, bestSizes[op.ordinal()], size));
			}
			bestSizes[op.ordinal()] = size;
		}
	}
}
