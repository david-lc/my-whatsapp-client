package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class SubscribeCallback implements MqttCallback {
    private String agent;
    private UserInfo userInfo;

    public SubscribeCallback(String agent, UserInfo userInfo){
        this.agent = agent;
        this.userInfo = userInfo;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Se desconectó");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //Deserializar mensaje
        ByteArrayInputStream bais = new ByteArrayInputStream(message.getPayload());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message dto = (Message) ois.readObject();
        ois.close();
        bais.close();

        //Procesar mensaje
        String messageText = userInfo.processMessage(dto);
        if(messageText != null)
            System.out.println("\nMensaje de " + this.agent + ": " + messageText);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
}
