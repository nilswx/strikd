package strikd.words;

import java.io.File;

import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundleManager;
import strikd.locale.LocaleBundle.DictionaryType;

public class StaticLocale
{
	static
	{
		LocaleBundleManager locMgr = new LocaleBundleManager(new File("locale.test"));
		locMgr.reload();
		LocaleBundle enUS = locMgr.getBundle("en_US");
		dict = enUS.getDictionary(DictionaryType.GENERATOR);
	}
	
	private static final WordDictionary dict;
	
	public static final WordDictionary getDictionary()
	{
		return dict;
	}
}
