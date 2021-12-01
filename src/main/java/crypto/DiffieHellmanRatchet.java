package crypto;
import javax.crypto.Mac;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class DiffieHellmanRatchet extends Ratchet {

    public DiffieHellmanRatchet(BigInteger key) {
        super(key);
    }

    @Override
    public BigInteger iterate() {
        return null;
    }
}
