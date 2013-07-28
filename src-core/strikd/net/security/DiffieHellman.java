package strikd.net.security;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.spec.DHParameterSpec;

import org.apache.log4j.Logger;

public class DiffieHellman
{
	private static final Logger logger = Logger.getLogger(DiffieHellman.class);
	
	private DHParameterSpec params;
	private BigInteger privateKey;
	private BigInteger publicKey;
    
	private DiffieHellman(DHParameterSpec params)
	{
		// Set parameters (p/g/bitlength)
		this.params = params;		

		// Generate our private key, a
		this.privateKey = BigInteger.probablePrime(this.params.getL(), new SecureRandom());
		
		// Generate our public key A (g^a mod p)
		this.publicKey = params.getG().modPow(this.privateKey, params.getP());
	}
	
	public BigInteger solveSharedSecret(BigInteger theirPublicKey)
	{
		// B^a mod p
		return theirPublicKey.modPow(this.privateKey, this.params.getP());
	}
	
	public DHParameterSpec getParams()
	{
		return this.params;
	}
	
	public BigInteger getPublicKey()
	{
		return this.publicKey;
	}
	
	private static AlgorithmParameterGenerator generator;
	
	static
	{
		try
		{
			generator = AlgorithmParameterGenerator.getInstance("DH");
			generator.init(512);
		}
		catch(NoSuchAlgorithmException e)
		{
			logger.error("could not setup Diffie-Hellman generator", e);
			System.exit(-1);
		}
	}
	
	public static DiffieHellman createKeyExchange()
	{
		try
		{
			DHParameterSpec params = generator.generateParameters().getParameterSpec(DHParameterSpec.class);
			return new DiffieHellman(params);
		}
		catch(InvalidParameterSpecException e)
		{
			logger.error("could not generate Diffie-Hellman parameters", e);
			return null;
		}
	}

}
