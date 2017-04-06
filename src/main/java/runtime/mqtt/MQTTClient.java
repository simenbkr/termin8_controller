package runtime.mqtt;

import org.fusesource.mqtt.client.*;

import java.net.URISyntaxException;

public class MQTTClient {

    private static MQTTClient instance = new MQTTClient();
    private MQTT client;
    private static final String HOST = "termin8.tech";
    private static final int PORT = 8883;
    private static final String username = "termin8";
    private static final String password = "jeghaterbarnmedraraksent";
    private static final short keepAliveTimer = 300;
    private static final String lastWillTopic = "controller/last_will";
    private static final String lastWillMessage = "Snakkes aldri";


    private MQTTClient(){
        try {
            this.client = new MQTT();
            this.client.setHost(HOST, PORT);
            this.client.setUserName(username);
            this.client.setPassword(password);
            this.client.setKeepAlive(keepAliveTimer);
            this.client.setWillTopic(lastWillTopic);
            this.client.setWillMessage(lastWillMessage);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private BlockingConnection createConnection(){
        BlockingConnection connection;
        try {
            connection = this.client.blockingConnection();
            connection.connect();
            return connection;
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BlockingConnection getConnection(){
        return instance.createConnection();
    }

    public static void main(String[] args) throws Exception {
        /*
        Example code of usage below!
        */

        BlockingConnection kobling = MQTTClient.getConnection();
        System.out.println(kobling.isConnected());

        Topic[] topics = {new Topic("foo", QoS.AT_LEAST_ONCE)};

        byte[] qoses = kobling.subscribe(topics);

        kobling.publish("foo", "Hello".getBytes(), QoS.AT_LEAST_ONCE, false);

        Message message = kobling.receive();
        System.out.println(message.getTopic());
        byte[] payload = message.getPayload();
        System.out.println(new String(payload, "UTF-8"));
        message.ack();


    }
}
