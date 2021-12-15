package crypto;
import at.favre.lib.crypto.HKDF;
import common.Constants;

public class DiffieHellmanRatchet extends Ratchet {
    HKDF hkdf;

    public DiffieHellmanRatchet(byte[] rootKey) {
        super(rootKey);
    }

    protected void generateMAC() {
        hkdf = HKDF.fromHmacSha512();
    }

    @Override
    protected byte[] obtainBytes(byte[] input) {
        return hkdf.extractAndExpand(rootKey, input, Constants.DH_RATCHET_INFO, 32);
    }
}
