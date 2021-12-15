package mqtt;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    private String senderId;
    private PublicKey publicKey;
    private byte[] payload;

    public Message(String senderId, PublicKey publicKey, byte[] payload) {
        this.senderId = senderId;
        this.publicKey = publicKey;
        this.payload = payload;
    }

    public String getSenderId() {
        return senderId;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getPayload() {
        return payload;
    }
}
