package crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;

import common.Constants;


public class DiffieHellmanKey {
    private PublicKey pubKey;
    private PrivateKey privKey;

    public DiffieHellmanKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(Constants.DIFFIE_HELLMAN_P, Constants.DIFFIE_HELLMAN_G);
        kpg.initialize(param);
        KeyPair kp = kpg.generateKeyPair();

        pubKey = kp.getPublic();
        privKey = kp.getPrivate();
}

    public PublicKey getPubKey() {
        return pubKey;
    }

    public byte[] getSharedKey(PublicKey otherPubKey) throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement;

        keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(privKey);
        keyAgreement.doPhase(otherPubKey, true);

        return keyAgreement.generateSecret();

    }

}
