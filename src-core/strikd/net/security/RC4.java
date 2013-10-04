package strikd.net.security;

import java.util.UUID;

public final class RC4
{
	private static final int TABLE_SIZE = 256;

	private int i;
	private int j;
	private final short[] table = new short[TABLE_SIZE];

	public RC4(byte[] key)
	{
		this.setKey(key);
	}

	public final void setKey(byte[] key)
	{
		this.i = 0;
		while(this.i < TABLE_SIZE)
		{
			this.table[this.i] = (short)this.i;
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
		short swap = this.table[a];
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
		System.out.println("TABLE DUMP");
		for(int x = 0; x < TABLE_SIZE; x++)
		{
			System.out.println(table[x]);
		}
		System.out.println();
		
		System.out.println("CIPHERING");
		byte[] result = new byte[data.length];
		for(int a = 0; a < data.length; a++)
		{
			result[a] = this.cipher(data[a]);
			System.out.println(data[a] + " -> " + result[a]);
		}
		System.out.println();

		return result;
	}

	public final void cipherDirect(byte[] data)
	{
		for(int a = 0; a < data.length; a++)
		{
			data[a] = (byte) (this.cipher(data[a]));
		}
	}
	
	public static void main(String[] args)
	{
		// Mama Appelsap test
		String key = "di8fh8328h477hf32";
		RC4 enc = new RC4(key.getBytes());
		RC4 dec = new RC4(key.getBytes());
		
		// Enc & dec
		String plain = "mama appelsap";
		String ed = new String(enc.cipher(plain.getBytes()));
		String dd = new String(dec.cipher(ed.getBytes()));
		
		// Brute for holes
		for(int i = 0; i < 10000; i++)
		{
			// Setup enc + dec
			key = UUID.randomUUID().toString();
			enc = new RC4(key.getBytes());
			dec = new RC4(key.getBytes());
			
			// Run encryption a few times
			for(int j = 0; j < 50; j++)
			{
				plain = UUID.randomUUID().toString();
				byte[] encd = enc.cipher(plain.getBytes());
				byte[] decd = dec.cipher(encd);
				String decdStr = new String(decd);
			
				// Evaluate result
				if(!decdStr.equals(plain))
				{
					System.out.println(decdStr + " fuck");
					return;
				}
				else
				{
					System.out.println(plain);
				}
			}
		}
		
		System.out.println("done");
	}
}