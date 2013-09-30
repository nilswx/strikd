package strikd.game.util;

public final class BitPacker
{
	public static final byte packNibbles(byte a, byte b)
	{
		return (byte)(a << 4 | b);
	}
	
	public static final byte[] unpackNibbles(byte packed)
	{
		return new byte[] { (byte)(packed >> 4), (byte)(packed & 0x0F) };
	}
}
