package strikd;

public class Version
{
	// TODO: return from .pom or something
	private static final String version = "0.4.3-dev";
	
	private Version() { }
	
	public static final String getVersion()
	{
		return version;
	}
}
