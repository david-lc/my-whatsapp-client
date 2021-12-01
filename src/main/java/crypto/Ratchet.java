package crypto;

import javax.crypto.Mac;
import java.math.BigInteger;

public abstract class Ratchet {
    protected BigInteger rootKey;

    public Ratchet(BigInteger key) {
        rootKey = key;
    }

    public abstract BigInteger iterate();
}
