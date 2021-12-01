package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
        System.out.println("\nMensaje de " + this.agent + ": " + message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
}
