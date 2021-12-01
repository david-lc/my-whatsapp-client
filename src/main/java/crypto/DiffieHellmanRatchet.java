package crypto;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import at.favre.lib.crypto.HKDF;

public class DiffieHellmanRatchet extends Ratchet {
    HKDF hkdf;

    public DiffieHellmanRatchet(byte[] rootKey) {
        super(rootKey);
    }

    protected void generateMAC() {
        hkdf = HKDF.fromHmacSha512();
    }

    @Override
    public byte[] iterate(byte[] input) {
        byte[] result = hkdf.extractAndExpand(rootKey, input, "hola".getBytes(StandardCharsets.UTF_8), 256);

        for (byte b: result) {
            System.out.println(b);
        }
        rootKey = Arrays.copyOfRange(result, 0, 127);

        return Arrays.copyOfRange(result, 128, 255);
    }
}
