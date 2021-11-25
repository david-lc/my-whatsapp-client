package mqtt;

import common.Constants;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public final class MQTT {
    private String senderId, receiverId, publicationChannel, subscriptionChannel;
    private MqttClient subscriber, publisher;

    public MQTT(String senderId, String receiverId, String publicationChannel, String subscriptionChannel) throws MqttException {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.publicationChannel = publicationChannel;
        this.subscriptionChannel = subscriptionChannel;

        this.startPublisher();
        this.startSubscriber();
    }

    private void startPublisher() throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.publisher = new MqttClient(Constants.BROKER_URL, this.senderId, session);
        this.publisher.connect();
    }

    private void startSubscriber() throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.subscriber = new MqttClient(Constants.BROKER_URL, this.senderId, session);
        this.subscriber.setCallback(new SubscribeCallback());
        this.subscriber.connect();
        this.subscriber.subscribe(this.subscriptionChannel);
    }

    public void sendMessage(String textMessage) throws MqttException {
        MqttMessage message = new MqttMessage(textMessage.getBytes());
        message.setQos(2);

        this.publisher.publish(this.publicationChannel, message);
    }
}
