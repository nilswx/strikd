package strikd.storage;

import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

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
		JacksonMapper.Builder builder = new JacksonMapper.Builder();
		
		// Config auto-detection
		builder.setVisibilityChecker(VisibilityChecker.Std.defaultInstance()
				
				// Disable all auto-detection
				.withVisibility(PropertyAccessor.ALL, Visibility.NONE)
				
				// Only serializic public fields by default
				.withVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY));
				
		
		// To save space on enums
		builder.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
		
		return builder.build();
	}
}
