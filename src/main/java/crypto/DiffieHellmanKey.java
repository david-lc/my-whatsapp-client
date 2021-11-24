package crypto;

import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import common.Constants;


public class DiffieHellmanKey {
    private BigInteger pubKey;
    private BigInteger privKey;

    public DiffieHellmanKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
            DHParameterSpec param = new DHParameterSpec(Constants.DIFFIE_HELLMAN_P, Constants.DIFFIE_HELLMAN_G);
            kpg.initialize(param);
            KeyPair kp = kpg.generateKeyPair();

            KeyFactory kfactory = KeyFactory.getInstance("DiffieHellman");

            DHPublicKeySpec publicKeySpec = kfactory.getKeySpec(kp.getPublic(), DHPublicKeySpec.class);
            DHPrivateKeySpec privateKeySpec = kfactory.getKeySpec(kp.getPrivate(), DHPrivateKeySpec.class);

            pubKey = publicKeySpec.getY();
            privKey = privateKeySpec.getX();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigInteger getPubKey() {
        return pubKey;
    }

    public void setPubKey(BigInteger pubKey) {
        this.pubKey = pubKey;
    }

    public BigInteger getPrivKey() {
        return privKey;
    }

    public void setPrivKey(BigInteger privKey) {
        this.privKey = privKey;
    }

}
