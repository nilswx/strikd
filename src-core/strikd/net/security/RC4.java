package strikd.net.security;

public final class RC4
{
	private static final int TABLE_SIZE = 256;

	private int i;
	private int j;
	private final int[] table = new int[TABLE_SIZE];

	public RC4(byte[] key)
	{
		this.setKey(key);
	}

	public final void setKey(byte[] key)
	{
		this.i = 0;
		while(this.i < TABLE_SIZE)
		{
			this.table[this.i] = this.i;
			this.i++;
		}

		this.i = this.j = 0;
		while(this.i < TABLE_SIZE)
		{
			this.j = (((this.j + this.table[this.i]) + key[(this.i % key.length)] & 0xff) % TABLE_SIZE);
			this.swap(this.i, this.j);
			this.i++;
		}

		this.i = this.j = 0;
	}

	private final int swap(int a, int b)
	{
		int swap = this.table[a];
		this.table[a] = this.table[b];
		this.table[b] = swap;

		return ((this.table[this.i] + this.table[this.j]) % TABLE_SIZE);
	}
	
	public final byte cipher(final byte data)
	{
		this.i = ((this.i + 1) % TABLE_SIZE);
		this.j = ((this.j + this.table[this.i]) % TABLE_SIZE);
		return (byte) (data & 0xff ^ this.table[this.swap(this.i, this.j)]);
	}

	public final byte[] cipher(byte[] data)
	{
		byte[] result = new byte[data.length];
		for(int a = 0; a < data.length; a++)
		{
			result[a] = this.cipher(data[a]);
		}

		return result;
	}

	public final void cipherDirect(byte[] data)
	{
		for(int a = 0; a < data.length; a++)
		{
			data[a] = (byte) (this.cipher(data[a]));
		}
	}
}