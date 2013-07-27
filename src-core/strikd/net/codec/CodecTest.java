package strikd.net.codec;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.msgpack.MessagePack;

public class CodecTest
{
	public static void main(String[] args) throws Exception
	{
		StrikMessage msg = new StrikMessage("HELLO");
		msg.set("piep", 43435);
		msg.set("hoii", "rawr");
		
		MessageEncoder enc = new MessageEncoder();
		MessageDecoder dec = new MessageDecoder();
		
		Object encd = enc.encode(null, null, msg);
		//Object decd = dec.decode(null, null, (ChannelBuffer) encd);
		
		Map<String, String> lol = new HashMap<String, String>();
		lol.put("dfadf", "pieep");
		lol.put("adfdf", "dddddd");
		byte[] d = MessagePack.pack(lol);
		
		Map<String, String> to = new HashMap<String, String>();
		Object up = MessagePack.unpack(d, to);
		
		up = null;
	}
}
