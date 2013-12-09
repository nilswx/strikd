package strikd.game.items.shop;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

public class AppStoreReceipts
{
	private final static String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
	private final static String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";
	
	private static final Logger logger = LoggerFactory.getLogger(AppStoreReceipts.class);
	
	public static JSONObject verifyReceipt(String receipt, boolean isSandbox)
	{
		// Create JSON object with Base64-encoded receipt
		JSONObject json = new JSONObject();
		json.put("receipt-data", BaseEncoding.base64().encode(receipt.getBytes()));
		
		try
		{
			// Create HTTPS connection to Apple
			HttpURLConnection conn = (HttpsURLConnection)new URL(isSandbox ? SANDBOX_URL : PRODUCTION_URL).openConnection();
			conn.setDoOutput(true);
			logger.debug("validating receipt at {}: \"{}\"", conn.getURL(), json);
			
			// Write request
			try(OutputStreamWriter request = new OutputStreamWriter(conn.getOutputStream()))
			{
				request.write(json.toString());
			}
			
			// Read + parse response 
			String str = CharStreams.toString(new InputStreamReader(conn.getInputStream(), Charsets.UTF_8));
			return new JSONObject(str);
		}
		catch (Exception e)
		{
			logger.error("error validating receipt", e);
		}

		return null;
	}
	
	public static void main(String[] args)
	{
		System.out.println(verifyReceipt("dafasdfasdf", true));
	}
}
