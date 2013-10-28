package strikd.util;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory
{
	private String format;
	private int counter;
	
	public NamedThreadFactory(String format)
	{
		this.format = format;
	}
	
	@Override
	public Thread newThread(Runnable runnable)
	{
		Thread thread = new Thread(runnable);
		thread.setName(String.format(this.format, ++this.counter));
		
		return thread;
	}
}
