package strikd.words;

public class WordCodec
{
	private static final byte ALPHABET_OFFSET = ((byte)'A' - 1);
	
	public static long encode(Word word)
	{
		long code = 0;
		
		char[] letters = word.letters();
		for(int p = 0; p < letters.length; p++)
		{
			// Use 5 bits per letter (2^5 = 32 possible letters, only 26 are used)
			//System.out.println("pack " + encodeLetter(letter));
		}
		return code & 0xFF;
	}
	
	public static Word decode(long code)
	{
		for(int p = 0; p < 12; p++)
		{
			// Shift them in blocks of 5 bits, stop when a code '0' is reached
		}
		
		return null;
	}
	
	public static final byte encodeLetter(char letter)
	{
		return (byte)(letter - ALPHABET_OFFSET);
	}
	
	public static final char decodeLetter(byte code)
	{
		return (char)(ALPHABET_OFFSET + code);
	}
	
	public static void main(String[] args)
	{
		System.out.println(decode(encode(Word.parse("DUCKSAUCE"))));
	}
}
