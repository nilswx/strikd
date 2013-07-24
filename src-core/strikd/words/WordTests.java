package strikd.words;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import strikd.locale.LocaleBundleManager;

public class WordTests
{
	@Test
	public void testLocale()
	{
		LocaleBundleManager localeMgr = new LocaleBundleManager(new File("locale"));
		assertNotNull(localeMgr.getBundle("en_US"));
	}
	
	@Test
	public void testPalindrome()
	{
		assertFalse(Word.parse("squirrel").isPalindrome());
		assertTrue(Word.parse("racecar").isPalindrome());
	}
}
