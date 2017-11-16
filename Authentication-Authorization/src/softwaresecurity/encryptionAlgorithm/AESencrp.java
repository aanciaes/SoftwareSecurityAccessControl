package softwaresecurity.encryptionAlgorithm;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESencrp {

    private static final String ALGO = "AES";
    private static final byte[] keyValue
            = new byte[]{'T', 'h', 'e', 'B', 'e', 's', '7', 'S', '$', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public static String encrypt(String Data, String privateKey) throws Exception {
        Key key = generateKey(privateKey);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        return java.util.Base64.getEncoder().encodeToString(encVal);
    }

    public static String decrypt(String encryptedData, String privateKey) throws Exception {
        Key key = generateKey(privateKey);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = java.util.Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    private static Key generateKey(String privateKey) throws Exception {
        Key key;
        if (privateKey.equals("")) {
            key = new SecretKeySpec(keyValue, ALGO);
        } else {
            key = new SecretKeySpec(privateKey.getBytes(), ALGO);
        }
        return key;
    }

    public static String generateRandom() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(128, random).toString(32).substring(0, 16);
    }
}
