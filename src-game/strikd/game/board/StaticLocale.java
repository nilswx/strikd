package strikd.game.board;

import java.io.File;

import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundleManager;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.words.WordDictionary;

public class StaticLocale
{
	public static void init() { }
	
	static
	{
		// Will be passed from higher on
		LocaleBundleManager locMgr = new LocaleBundleManager(new File("locale"));
		LocaleBundle enUS = locMgr.getBundle("en_US");
		dict = enUS.getDictionary(DictionaryType.GENERATOR);
	}
	
	public static final WordDictionary dict;
}
