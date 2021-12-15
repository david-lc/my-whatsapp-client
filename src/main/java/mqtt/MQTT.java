package mqtt;

import common.Constants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.PublicKey;

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

    public void sendMessage(PublicKey pk, byte[] payload) throws MqttException, IOException {
        Message dto = new Message(this.senderId, pk, payload);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dto);
        oos.flush();

        MqttMessage message = new MqttMessage(baos.toByteArray());
        message.setQos(2);

        oos.close();
        baos.close();

        this.client.publish(this.publicationChannel, message);
    }

    public void finishClient() throws MqttException {
        this.client.unsubscribe(this.subscriptionChannel);
        this.client.disconnect();
        this.client.close();
    }
}
