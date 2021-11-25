package common;

import mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Scanner;

public final class Menu {
    private static final Scanner input = new Scanner(System.in);

    public void display(){
        MQTT mqtt = null;
        int option = -1;

        while(option < 1 || option > 2) {
            try {
                System.out.print("Los interlocutores son: \n1) Alice\n2) Bob\n¿Quién eres? (1 o 2): ");
                option = Integer.parseInt(input.nextLine());

                if(option == 1)
                    mqtt = new MQTT(Constants.CLIENT_ALICE, Constants.CLIENT_BOB, Constants.CHANNEL_ALICE, Constants.CHANNEL_BOB);
                else if (option == 2)
                    mqtt = new MQTT(Constants.CLIENT_BOB, Constants.CLIENT_ALICE, Constants.CHANNEL_BOB, Constants.CHANNEL_ALICE);
                else
                    throw new NumberFormatException();
            }
            catch (NumberFormatException nfe){
                System.out.println("Introduzca un número válido (1 o 2).");
            } catch (MqttException me) {
                System.out.println("Error: " + me.getMessage());
            }
        }

    }
}
