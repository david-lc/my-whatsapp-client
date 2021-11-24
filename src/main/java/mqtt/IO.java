package mqtt;

import common.Constants;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public final class IO {
    private static final Scanner sc = new Scanner(System.in);
    private MqttClient subscriber, publisher;

    public void startSubscriber() throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.subscriber = new MqttClient(Constants.BROKER_URL, Constants.CLIENT_ID1, session);
        this.subscriber.setCallback(new SubscribeCallback());
        this.subscriber.connect();
        this.subscriber.subscribe(Constants.CHANNEL_IN);
    }

    public void startPublisher() throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.publisher = new MqttClient(Constants.BROKER_URL, Constants.CLIENT_ID2, session);
        this.publisher.connect();
    }

    public void sendMessage(String info) throws MqttException {
        System.out.print(info);
        String textMessage = sc.nextLine();

        MqttMessage message = new MqttMessage(textMessage.getBytes());
        message.setQos(2);

        this.publisher.publish(Constants.CHANNEL_OUT, message);
    }
}
