package common;

import crypto.AES;
import mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public final class Menu {
    private static final Scanner input = new Scanner(System.in);
    private MQTT mqtt;

    public void display(){
        try{
            this.identifyClient();
            this.startConversation();
            this.finish();

        } catch(MqttException me){
            System.out.println("Error de MQTT: " + me.getMessage());
        } catch(Exception e){
            System.out.println("Error general: " + e.getMessage());
        }
    }

    private void identifyClient() throws MqttException {
        int option = -1;

        while(option < 1 || option > 2) {
            try {
                System.out.print("Los interlocutores son: \n1) Alice\n2) Bob\n¿Quién eres? (1 o 2): ");
                option = Integer.parseInt(input.nextLine());

                if(option == 1)
                    this.mqtt = new MQTT(Constants.CLIENT_ALICE, Constants.CLIENT_BOB, Constants.CHANNEL_ALICE, Constants.CHANNEL_BOB);
                else if (option == 2)
                    this.mqtt = new MQTT(Constants.CLIENT_BOB, Constants.CLIENT_ALICE, Constants.CHANNEL_BOB, Constants.CHANNEL_ALICE);
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException nfe){
                System.out.println("Introduzca un número válido (1 o 2).");
            }
        }
    }

    private void startConversation() throws MqttException {
        System.out.println("Puede empezar a enviar mensajes:\n");

        String message = "";

        while(!message.equals("FIN")) {
            message = input.nextLine();
            this.mqtt.sendMessage(message);
        }
    }

    private void finish() throws MqttException {
        this.mqtt.finishClient();
    }
}
