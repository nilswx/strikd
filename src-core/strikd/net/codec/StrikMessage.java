package strikd.net.codec;

import java.util.HashMap;
import java.util.Map;

import org.msgpack.annotation.Message;

@Message
public class StrikMessage
{
	private final String op;
	private final Map<String, Object> data;
	
	public StrikMessage(String op)
	{
		this(op, new HashMap<String, Object>());
	}
	
	public StrikMessage(String op, Map<String, Object> data)
	{
		this.op = op;
		this.data = data;
	}
	
	public <T> void set(String key, T value)
	{
		this.data.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key)
	{
		return (T)this.data.get(key);
	}
	
	public String getOp()
	{
		return this.op;
	}

	public Map<String, Object> getData()
	{
		return this.data;
	}
	
	@Override
	public String toString()
	{
		return this.op + " " + this.data.toString();
	}
}
