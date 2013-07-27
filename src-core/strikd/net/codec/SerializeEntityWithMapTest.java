package strikd.net.codec;

import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.template.Template;
import org.msgpack.template.Templates;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.FloatValue;
import org.msgpack.type.IntegerValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;
import org.msgpack.unpacker.Unpacker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Verifies whether object that contains map string -> object could be
 * serialized.
 */
public class SerializeEntityWithMapTest
{

	@Message
	public static class Entity
	{
		public String name;
		public Map<String, Object> properties = new HashMap<String, Object>();
	}

	public static class EntityTemplate extends AbstractTemplate<Entity>
	{
		@Override
		public void write(Packer pk, Entity v, boolean required) throws IOException
		{
			if(v == null)
			{
				if(required)
				{
					throw new NullPointerException();
				}
				pk.writeNil();
				return;
			}
			// serialize entity object
			pk.writeArrayBegin(2);
			{
				// serialize 1st field "name"
				pk.write(v.name);
				// serialize 2nd field "properties"
				pk.writeMapBegin(v.properties.size());
				{
					// serialize each pair of key and value
					for(Map.Entry<String, Object> e : v.properties.entrySet())
					{
						String key = e.getKey();
						pk.write(key);
						Object val = e.getValue();
						pk.write(val);
					}
				}
				pk.writeMapEnd();
			}
			pk.writeArrayEnd();
		}

		@Override
		public Entity read(Unpacker u, Entity to, boolean required) throws IOException
		{
			if(!required && u.trySkipNil())
			{
				return null;
			}
			if(to == null)
			{
				to = new Entity();
			}
			// deserialize entity object
			u.readArrayBegin();
			{
				// deserialize 1st field "name"
				to.name = u.read(Templates.TString);
				// deserialize 2nd field "properties"
				int size = u.readMapBegin();
				to.properties = new HashMap<String, Object>();
				{
					// deserialize each pair of key and value
					for(int i = 0; i < size; i++)
					{
						// deserialize key
						String key = u.read(Templates.TString);
						// deserialize value
						Object val = toObject(u.readValue());
						to.properties.put(key, val);
					}
				}
				u.readMapEnd();
			}
			u.readArrayEnd();
			return to;
		}

		private static Object toObject(Value value) throws IOException
		{
			Converter conv = new Converter(value);
			if(value.isNilValue())
			{ // null
				return null;
			}
			else if(value.isRawValue())
			{
				return conv.read(Templates.TString);
			}
			else if(value.isBooleanValue())
			{ // boolean
				return conv.read(Templates.TBoolean);
			}
			else if(value.isIntegerValue())
			{ // int or long or BigInteger
				// deserialize value to int
				IntegerValue v = value.asIntegerValue();
				return conv.read(Templates.TInteger);
			}
			else if(value.isFloatValue())
			{ // float or double
				// deserialize value to double
				FloatValue v = value.asFloatValue();
				return conv.read(Templates.TDouble);
			}
			else if(value.isArrayValue())
			{ // List or Set
				// deserialize value to List object
				ArrayValue v = value.asArrayValue();
				List<Object> ret = new ArrayList<Object>(v.size());
				for(Value elementValue : v)
				{
					ret.add(toObject(elementValue));
				}
				return ret;
			}
			else if(value.isMapValue())
			{ // Map
				// deserialize value to Map object
				throw new UnsupportedOperationException(); // FIXME
			}
			else
			{
				throw new RuntimeException("fatal error");
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		Entity src = new Entity();
		src.name = "test";
		// String, Integer, Long, Double, Date,
		// List<String | Integer | Long | Double | Date>
		// Set<String | Integer, Long, Double, Date>
		src.properties.put("string", "aaaa");
		src.properties.put("integer", 10);
		src.properties.put("long", 10L);
		src.properties.put("double", 100.1);
		List<Object> list = new ArrayList<Object>();
		list.add("bbb");
		list.add(20);
		src.properties.put("list", list);

		MessagePack msgpack = new MessagePack();
		Template<Entity> tmpl = new EntityTemplate();
		// serialize
		byte[] bytes = msgpack.write(src, tmpl);
		// deserialize
		Entity dst = msgpack.read(bytes, tmpl);
		System.out.println(dst.properties);
	}
}