package crypto;

import javax.crypto.Mac;
import java.math.BigInteger;
import java.security.InvalidKeyException;

public abstract class Ratchet {
    protected byte[] rootKey;

    public Ratchet(byte[] key) {
        rootKey = key;
        generateMAC();
    }

    protected abstract void generateMAC();
    public abstract byte[] iterate(byte[] input) throws InvalidKeyException;
}
