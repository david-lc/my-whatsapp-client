import crypto.DiffieHellmanKey;

public class Main {
    public static void main(String[] args) {
        DiffieHellmanKey key = new DiffieHellmanKey();
        System.out.println(key.getPubKey());
        System.out.println(key.getPrivKey());
    }
}
