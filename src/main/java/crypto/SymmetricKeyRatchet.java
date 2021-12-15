package crypto;

import common.Constants;
import crypto.Ratchet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.util.Arrays;

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

    protected byte[] obtainBytes(byte[] input) throws InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec(rootKey, "RawBytes");
        mac.init(sks);
        mac.update(Constants.SYM_RATCHET_INFO);
        return mac.doFinal();

    }
}
