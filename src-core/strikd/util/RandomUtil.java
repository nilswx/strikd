package strikd.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil
{
	private static final ThreadLocalRandom getRng()
	{
		return ThreadLocalRandom.current();
	}
	
	public static boolean flipCoin()
	{
		return getBool(0.5);
	}
	
	public static boolean getBool(double p)
	{
		return (getRng().nextDouble() < p);
	}
	
	public static int pickInt(int min, int max)
	{
		return getRng().nextInt(min, max + 1);
	}

	public static <T> T pickOne(T[] elements)
	{
		int length = elements.length;
		if(length > 0)
		{
			return elements[getRng().nextInt(length)];
		}
		else
		{
			return null;
		}
	}

	public static <T> T pickOne(List<T> elements)
	{
		int size = elements.size();
		if(size > 0)
		{
			return elements.get(getRng().nextInt(size));
		}
		else
		{
			return null;
		}
	}

	public static <T> T pickOne(Collection<T> elements)
	{
		List<T> list = new ArrayList<T>(elements);
		return pickOne(list);
	}

    public static String randomizeString(String string)
    {
        StringBuilder randomStringBuilder = new StringBuilder();
        StringBuilder originalString = new StringBuilder(string);

        while(originalString.length() > 0)
        {
            // Get a random char from source
            int randomIndex = RandomUtil.pickInt(0, originalString.length() - 1);
            char randomChar = originalString.charAt(randomIndex);

            // Add it to the builder
            randomStringBuilder.append(randomChar);

            // Remove from source
            originalString.deleteCharAt(randomIndex);
        }

        return randomStringBuilder.toString();
    }

    public static char randomCharFromString(String string)
    {
        int randomIndex = RandomUtil.pickInt(0, string.length() - 1);
        return string.charAt(randomIndex);
    }
}
