package strikd.net.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.Opcodes;

public final class MessageAllocatorExpert
{
	private static final Logger logger = LoggerFactory.getLogger(MessageAllocatorExpert.class);
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
				logger.debug("{}: {} -> {} bytes", op, bestSizes[op.ordinal()], size);
			}
			bestSizes[op.ordinal()] = size;
		}
	}
}
