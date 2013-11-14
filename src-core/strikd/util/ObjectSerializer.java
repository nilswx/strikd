package strikd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class ObjectSerializer
{
	public static <T> void save(T obj, File file) throws Exception
	{
		Output output = new Output(new FileOutputStream(file));
		new Kryo().writeClassAndObject(output, obj);
		output.flush();
		output.close();
	}
	
	public static <T> T load(File file) throws Exception
	{
		Input input = new Input(new FileInputStream(file));
		@SuppressWarnings("unchecked")
		T obj = (T) new Kryo().readClassAndObject(input);
		input.close();
		
		return obj;
	}
}
