package crypto;

import common.Constants;
import crypto.Ratchet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;

public class SymmetricKeyRatchet extends Ratchet {
    private Mac mac;

    public SymmetricKeyRatchet(byte[] key) {
        super(key);
    }

    protected void generateMAC() {
        try {
            mac = Mac.getInstance("HmacSHA512");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] iterate(byte[] input) throws InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(rootKey, "RawBytes");
        mac.init(sks);
        mac.update(Constants.SYM_RATCHET_INFO);
        byte[] result = mac.doFinal();

        System.out.println(result.length);
        return null;
    }
}
