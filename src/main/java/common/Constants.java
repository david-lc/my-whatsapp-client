package common;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class Constants {
    public static final String BROKER_URL = "tcp://52.209.186.238";
    public static final String CLIENT_ALICE = "Alice";
    public static final String CLIENT_BOB = "Bob";
    public static final String CHANNEL_ALICE = "landin.soler.in";
    public static final String CHANNEL_BOB = "landin.soler.out";

    public static final byte[] INITIAL_ROOT_KEY = HexFormat.of().parseHex("26eb8ba57e8e4e64aa2ccbfe1b1bf62c");

    public static final BigInteger DIFFIE_HELLMAN_G = new BigInteger("2", 16);
    public static final BigInteger DIFFIE_HELLMAN_P = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);

    public static final byte[] DH_RATCHET_INFO = "1234".getBytes(StandardCharsets.UTF_8);

    public static final byte[] SYM_RATCHET_INFO = "5678".getBytes(StandardCharsets.UTF_8);
    public static final byte[] NULL_BYTE_ARRAY = "0".getBytes(StandardCharsets.UTF_8);

    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    public static final int SYMMETRIC_KEY_BYTES = 16;
    public static final int GCM_TAG_BYTES = 16;
    public static final int IV_BYTES = 12;

}
