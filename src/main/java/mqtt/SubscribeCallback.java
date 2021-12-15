package mqtt;

import crypto.AES;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SubscribeCallback implements MqttCallback {
    private String agent;

    public SubscribeCallback(String agent){
        this.agent = agent;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.printf("Se desconectó");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(message.getPayload());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message dto = (Message) ois.readObject();
        ois.close();
        bais.close();

        if(dto.getPayload() != null)
        {

        }
        else
        {

        }

        System.out.println("\nMensaje de " + this.agent + ": " + message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
}
