package crypto;

import javax.crypto.Mac;
import java.math.BigInteger;

public class Ratchet {
    protected Mac MAC;
    protected BigInteger RootKey;
}
