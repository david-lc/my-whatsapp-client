package common;

import mqtt.MQTT;
import mqtt.Message;
import mqtt.UserInfo;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public final class Menu {
    private static final Scanner input = new Scanner(System.in);
    private MQTT mqtt;
    private UserInfo userInfo;

    public void display() {
        try {
            userInfo = new UserInfo();
            this.identifyClient();
            this.startConversation();
            this.finish();

        } catch (MqttException me) {
            System.out.println("Error de MQTT: " + me);
        } catch (InvalidAlgorithmParameterException iape) {
            System.out.println("Error de parámetro: " + iape.getMessage());
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("Error de algoritmo: " + nsae.getMessage());
        } catch (Exception e) {
            System.out.println("Error general: " + e.getMessage());
        }
    }

        private void identifyClient() throws MqttException {
        int option = -1;

        System.out.print("Quiere iniciar el programa en modo \"DEBUG\" (S/n): ");
        String debug = input.nextLine();
        boolean debugMode = debug.equals("S") || debug.equals("s");

        userInfo.setDebug(debugMode);
        while(option < 1 || option > 2) {
            try {
                System.out.print("Los interlocutores son: \n1) Alice\n2) Bob\n¿Quién eres? (1 o 2): ");
                option = Integer.parseInt(input.nextLine());

                if(option == 1)
                    this.mqtt = new MQTT(Constants.CLIENT_ALICE, Constants.CLIENT_BOB, Constants.CHANNEL_ALICE, Constants.CHANNEL_BOB, userInfo);
                else if (option == 2)
                    this.mqtt = new MQTT(Constants.CLIENT_BOB, Constants.CLIENT_ALICE, Constants.CHANNEL_BOB, Constants.CHANNEL_ALICE, userInfo);
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException nfe){
                System.out.println("Introduzca un número válido (1 o 2).");
            }
        }
    }

    private void startConversation() throws MqttException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //Crear mensaje inicial
        Message initMessage = userInfo.createEmptyMessage();
        this.mqtt.sendMessage(initMessage);

        System.out.println("Puede empezar a enviar mensajes:\n");

        String messageText = "";

        while(!messageText.equals("FIN")) {
            //Leer entrada
            messageText = input.nextLine();

            try {
                //Crear mensaje a partir de la entrada
                Message message = userInfo.createMessage(messageText);
                this.mqtt.sendMessage(message);

            } catch (NoSuchFieldException nsfe) {
                System.out.println("Excepción de clave compartida: " + nsfe.getMessage());
            }
        }
    }

    private void finish() throws MqttException {
        this.mqtt.finishClient();
    }
}
