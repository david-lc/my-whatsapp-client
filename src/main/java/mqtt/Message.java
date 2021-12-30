package mqtt;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private PublicKey publicKey;
    private byte[] iv;
    private byte[] payload;

    public Message(PublicKey publicKey, byte[] iv, byte[] payload) {
        this.publicKey = publicKey;
        this.iv = iv;
        this.payload = payload;
    }

    public Message(PublicKey publicKey) {
        this.publicKey = publicKey;
        this.iv = null;
        this.payload = null;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getPayload() {
        return payload;
    }

    public byte[] getIv() { return iv; }
}
