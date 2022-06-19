package security;

import javax.crypto.Cipher;
import java.security.*;


public class AsymmetricEncr {
	private static final String RSA = "RSA";

	public static KeyPair generaChiavi() throws Exception{
	    SecureRandom secureRandom = new SecureRandom();
	    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
	    keyPairGenerator.initialize(2048, secureRandom);
	    return keyPairGenerator.generateKeyPair();
	}

	public static byte[] cripta(String testoInChiaro, PublicKey chiavePubblica) throws Exception{
	    Cipher cipher = Cipher.getInstance(RSA);
	    cipher.init(Cipher.ENCRYPT_MODE, chiavePubblica);
	    return cipher.doFinal(testoInChiaro.getBytes());
	}
		
	public static String decripta(byte[] testoCifrato, PrivateKey chiavePrivata) throws Exception {
	    Cipher cipher = Cipher.getInstance(RSA);
	    cipher.init(Cipher.DECRYPT_MODE, chiavePrivata);
	    byte[] risultato = cipher.doFinal(testoCifrato);
	    return new String(risultato);
	}
}