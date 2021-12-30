package mqtt;

import common.Constants;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class MQTT {
    private String senderId, receiverId, publicationChannel, subscriptionChannel;
    private IMqttClient client;

    public MQTT(String senderId, String receiverId, String publicationChannel, String subscriptionChannel, UserInfo userInfo) throws MqttException {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.publicationChannel = publicationChannel;
        this.subscriptionChannel = subscriptionChannel;

        this.startClient(userInfo);
    }

    private void startClient(UserInfo userInfo) throws MqttException {
        MemoryPersistence session = new MemoryPersistence();

        this.client = new MqttClient(Constants.BROKER_URL, this.senderId, session);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setConnectionTimeout(10);
        this.client.connect(options);

        this.client.setCallback(new SubscribeCallback(this.receiverId, userInfo));
        this.client.subscribe(this.subscriptionChannel);
    }

    public void sendMessage(Message dto) throws MqttException, IOException {
        //Serializar mensaje
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dto);
        oos.flush();

        //Enviar mensaje
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
