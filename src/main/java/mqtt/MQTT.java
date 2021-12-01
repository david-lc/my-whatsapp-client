package mqtt;

import common.Constants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public final class MQTT {
    private String senderId, receiverId, publicationChannel, subscriptionChannel;
    private IMqttClient client;

    public MQTT(String senderId, String receiverId, String publicationChannel, String subscriptionChannel) throws MqttException {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.publicationChannel = publicationChannel;
        this.subscriptionChannel = subscriptionChannel;

        this.startClient();
    }

    private void startClient() throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.client = new MqttClient(Constants.BROKER_URL, this.senderId, session);
        this.client.connect();

        this.client.setCallback(new SubscribeCallback(this.receiverId));
        this.client.subscribe(this.subscriptionChannel);
        //MqttConnectOptions options = new MqttConnectOptions();
        //options.setAutomaticReconnect(true);
        //options.setCleanSession(true);
        //options.setConnectionTimeout(10);
        //publisher.connect(options);
    }

    public void sendMessage(String textMessage) throws MqttException {
        MqttMessage message = new MqttMessage(textMessage.getBytes());
        message.setQos(2);

        this.client.publish(this.publicationChannel, message);
    }

    public void finishClient() throws MqttException {
        this.client.unsubscribe(this.subscriptionChannel);
        this.client.disconnect();
        this.client.close();
    }
}
