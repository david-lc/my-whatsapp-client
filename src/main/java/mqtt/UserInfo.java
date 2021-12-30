package mqtt;

import common.Constants;
import crypto.AES;
import crypto.DiffieHellmanKey;
import crypto.DiffieHellmanRatchet;
import crypto.SymmetricKeyRatchet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

public class UserInfo {
    private PublicKey otherPK;
    private DiffieHellmanKey selfKey;
    private DiffieHellmanRatchet dhRatchet;
    private SymmetricKeyRatchet receiveSymRatchet;
    private SymmetricKeyRatchet sendSymRatchet;

    public UserInfo() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {

        //Crear claves iniciales y DHRatchet
        selfKey = new DiffieHellmanKey();
        dhRatchet = new DiffieHellmanRatchet(Constants.INITIAL_ROOT_KEY);
    }

    public Message createMessage(String messageText) throws NoSuchFieldException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException {
        //No se puede crear mensaje hasta que se conozca la clave publica del interlocutor
        if(otherPK == null) {
            throw new NoSuchFieldException("Aun no se ha recibido la clave publica del interlocutor");
        }

        //Cifrar contenido
        byte[] messageKey = sendSymRatchet.iterate(Constants.NULL_BYTE_ARRAY);
        System.out.println("Ratchet de envío: clave generada -> " + AES.bytesToHexString(messageKey));

        byte[] iv = AES.generateIVBytes();
        System.out.println("Cliente: IV generado -> " + AES.bytesToHexString(iv));

        SecretKey key = new SecretKeySpec(messageKey, "AES");
        byte[] payload = AES.encrypt(messageText.getBytes(StandardCharsets.UTF_8), key, iv);

        //Devolver mensaje con el contenido cifrado
        return new Message(selfKey.getPubKey(), iv, payload);
    }

    public Message createEmptyMessage() {
        System.out.println("Mensaje inicial enviado");
        //Devolver mensaje vacio
        return new Message(selfKey.getPubKey());
    }

    public String processMessage(Message message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        PublicKey newPK = message.getPublicKey();
        //Primera iteracion, o recibir nueva clave publica
        if((otherPK == null) || (!Arrays.equals(newPK.getEncoded(), otherPK.getEncoded()))) {
            otherPK = newPK;
            System.out.println("Cliente: nueva PK recibida -> " + AES.bytesToHexString(otherPK.getEncoded()));

            //Calcular secreto compartido e iterar el DHRatchet
            byte[] sharedSecret = selfKey.getSharedKey(newPK);
            System.out.println("Cliente: secreto DH calculado -> " + AES.bytesToHexString(sharedSecret));

            byte[] symRoot = dhRatchet.iterate(sharedSecret);
            System.out.println("Ratchet DH: clave raíz generada para los ratchets simétricos -> " + AES.bytesToHexString(symRoot));

            receiveSymRatchet = new SymmetricKeyRatchet(symRoot);

            //Crear nuevas claves DH e iterar de nuevo el DHRatchet
            // dhRatchet = new DiffieHellmanRatchet(Constants.INITIAL_ROOT_KEY);
            // byte[] sendSymRoot = dhRatchet.iterate(sharedSecret);
            sendSymRatchet = new SymmetricKeyRatchet(symRoot);
        }

        byte[] payload = message.getPayload();

        if(payload != null) {
            System.out.println("Mensaje recibido: " + payload);
            byte[] messageKey = receiveSymRatchet.iterate(Constants.NULL_BYTE_ARRAY);
            SecretKey key = new SecretKeySpec(messageKey, "AES");

            System.out.println("Ratchet de recepción: clave generada -> " + AES.bytesToHexString(key.getEncoded()));

            byte[] iv = message.getIv();

            System.out.println("Cliente: IV recuperado -> " + AES.bytesToHexString(iv));

            byte[] decodedPayload = AES.decrypt(payload, key, iv);
            String decodedText = new String(decodedPayload);

            return decodedText;
        }
        else return null;
    }
}
