package strikd.storage;

import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;

/**
 * This is the Jackson mapper that will be used to (de)serialize all objects from the database.
 * Changes here can possibly break ALL existing database objects, so ONLY edit this if you KNOW what you are doing.
 * 
 * @author nilsw
 *
 */
public class DataMapper
{
	public static Mapper getMapper()
	{
		return new JacksonMapper.Builder().build();
	}
}
