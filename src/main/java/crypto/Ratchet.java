package crypto;

import javax.crypto.Mac;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.util.Arrays;

public abstract class Ratchet {
    protected byte[] rootKey;

    public Ratchet(byte[] key) {
        rootKey = key;
        generateMAC();
    }

    protected abstract void generateMAC();
    public byte[] iterate(byte[] input) throws InvalidKeyException {
        byte[] result = this.obtainBytes(input);

        rootKey = Arrays.copyOfRange(result, 0, 15);
        return Arrays.copyOfRange(result, 16, 31);
    }

    protected abstract byte[] obtainBytes(byte[] input) throws InvalidKeyException;
}
