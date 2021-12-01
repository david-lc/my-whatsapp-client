package crypto;

import crypto.Ratchet;

import javax.crypto.Mac;
import java.math.BigInteger;

public class SymmetricKeyRatchet extends Ratchet {
    private Mac MAC;

    public SymmetricKeyRatchet(BigInteger key) {
        super(key);
        generateMAC();
    }

    private void generateMAC() {
        try {
            MAC = Mac.getInstance("HmacSHA512");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigInteger iterate() {
        return null;
    }
}
