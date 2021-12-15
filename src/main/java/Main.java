import common.Constants;
import common.Menu;
import crypto.DiffieHellmanRatchet;
import crypto.SymmetricKeyRatchet;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        SymmetricKeyRatchet sr = new SymmetricKeyRatchet(Constants.DIFFIE_HELLMAN_P.toByteArray());
        try {
            sr.iterate("AAAAAA".getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e) {

        }
        (new Menu()).display();
    }
}
