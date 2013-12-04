package strikd;

public class Version
{
	private static final String version = "0.4.1-dev";
	
	private Version() { }
	
	public static final String getVersion()
	{
		return version;
	}
}
