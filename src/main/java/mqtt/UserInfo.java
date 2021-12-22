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
        byte[] iv = AES.generateIVBytes();
        SecretKey key = new SecretKeySpec(messageKey, "AES");
        byte[] payload = AES.encrypt(messageText.getBytes(StandardCharsets.UTF_8), key, iv);

        //Devolver mensaje con el contenido cifrado
        return new Message(selfKey.getPubKey(), payload);

    }

    public Message createEmptyMessage() {

        //Devolver mensaje vacio
        return new Message(selfKey.getPubKey(), null);
    }

    public String processMessage(Message message) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        PublicKey newPK = message.getPublicKey();
        //Primera iteracion, o recibir nueva clave publica
        if((otherPK == null) || (!Arrays.equals(newPK.getEncoded(), otherPK.getEncoded()))) {

            otherPK = newPK;

            //Calcular secreto compartido e iterar el DHRatchet
            byte[] sharedSecret = selfKey.getSharedKey(newPK);
            byte[] receiveSymRoot = dhRatchet.iterate(sharedSecret);
            receiveSymRatchet = new SymmetricKeyRatchet(receiveSymRoot);

            //Crear nuevas claves DH e iterar de nuevo el DHRatchet
            dhRatchet = new DiffieHellmanRatchet(Constants.INITIAL_ROOT_KEY);
            byte[] sendSymRoot = dhRatchet.iterate(sharedSecret);
            sendSymRatchet = new SymmetricKeyRatchet(sendSymRoot);
        }
        byte[] payload = message.getPayload();
        if(payload != null) {
            byte[] messageKey = receiveSymRatchet.iterate(Constants.NULL_BYTE_ARRAY);
            SecretKey key = new SecretKeySpec(messageKey, "AES");

            //IMPORTANTE: CAMBIAR ESTO
            //ES NECESARIO TRANSMITIR EL VECTOR DE INICIALIZACION (CREO NO????)
            byte[] iv = AES.generateIVBytes();
            //REPITO, ESTO HAY QUE CAMBIARLO
            //VER FUNCION "CREATEMESSAGE", QUE ES DONDE SE GENERA EL IV

            byte[] decodedPayload = AES.decrypt(payload, key, iv);
            String decodedText = new String(decodedPayload);

            return decodedText;
        }
        else return null;
    }

    public DiffieHellmanKey getSelfKey() {
        return selfKey;
    }

}
