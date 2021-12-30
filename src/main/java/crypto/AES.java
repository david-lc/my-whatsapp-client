package crypto;

import common.Constants;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class AES {
    public static byte[] encrypt(byte[] m, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        GCMParameterSpec gcm = new GCMParameterSpec(Constants.GCM_TAG_BYTES * 8, iv);

        Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcm);

        return cipher.doFinal(m);
    }

    public static byte[] decrypt(byte[] c, SecretKey key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        GCMParameterSpec gcm = new GCMParameterSpec(Constants.GCM_TAG_BYTES * 8, iv);

        Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, gcm);

        return cipher.doFinal(c);
    }

    public static byte[] generateIVBytes() {
        SecureRandom secureRandom = new SecureRandom();

        byte[] iv = new byte[Constants.IV_BYTES];
        secureRandom.nextBytes(iv);

        return iv;
    }

    // Método para convertir un array de bytes en un String hexadecimal
    public static String bytesToHexString(byte[] bytes){
        if(bytes != null) {
            char[] hexChars = new char[bytes.length * 2];

            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = Constants.HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = Constants.HEX_ARRAY[v & 0x0F];
            }

            return "0x".concat(new String(hexChars));
        }
        else
        {
            System.out.println("Advertencia: se recibió un array de bytes vacío.");
            return null;
        }
    }
}
