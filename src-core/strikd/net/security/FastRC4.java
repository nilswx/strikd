package strikd.net.security;

public final class FastRC4
{
	private static final int TABLE_SIZE = 256;

	private int i;
	private int j;
	private final short[] table = new short[TABLE_SIZE];

	public FastRC4(byte[] key)
	{
		this.setKey(key);
	}

	public final void setKey(byte[] key)
	{
		for(this.i = 0; this.i < TABLE_SIZE; this.i++)
		{
			this.table[this.i] = (short)this.i;
		}
		short swap;
		for(this.i = 0, this.j = 0; this.i < TABLE_SIZE; this.i++)
		{
			this.j = (((this.j + this.table[this.i]) + key[(this.i % key.length)]) % TABLE_SIZE);
			swap = this.table[this.i];
			this.table[this.i] = this.table[this.j];
			this.table[this.j] = swap;
		}
		this.i = this.j = 0;
	}

	public final byte[] cipher(byte[] data)
	{
		byte[] result = new byte[data.length];
		short swap;
		for(int a = 0; a < data.length; a++)
		{
			this.i = ((this.i + 1) % TABLE_SIZE);
			this.j = ((this.j + this.table[this.i]) % TABLE_SIZE);
			
			swap = this.table[this.i];
			this.table[this.i] = this.table[this.j];
			this.table[this.j] = swap;
			
			result[a] = (byte)(data[a] ^ this.table[(this.table[this.i] + this.table[this.j]) % TABLE_SIZE]);
		}
		
		return result;
	}

	public final void cipherDirect(byte[] data, int offset, int length)
	{
		short swap;
		for(int a = offset; a < length; a++)
		{
			this.i = ((this.i + 1) % TABLE_SIZE);
			this.j = ((this.j + this.table[this.i]) % TABLE_SIZE);
			
			swap = this.table[this.i];
			this.table[this.i] = this.table[this.j];
			this.table[this.j] = swap;
			
			data[a] ^= this.table[(this.table[this.i] + this.table[this.j]) % TABLE_SIZE];
		}
	}
}