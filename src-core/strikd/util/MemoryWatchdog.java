package strikd.util;

import org.apache.log4j.Logger;

public final class MemoryWatchdog implements Runnable
{
	private static final Logger logger = Logger.getLogger(MemoryWatchdog.class);
	
	private final Runtime vm;
	
	public MemoryWatchdog()
	{
		this.vm = Runtime.getRuntime();
	}
	
	@Override
	public void run()
	{
		logger.info(String.format("%.2f MiB", (this.vm.totalMemory() - this.vm.freeMemory()) / 1024f / 1024f));
	}
}
