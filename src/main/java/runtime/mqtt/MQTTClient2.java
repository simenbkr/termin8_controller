package runtime.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTClient2 {

    private MqttClient client;
    private static final String HOST = "tcp://termin8.tech";
    private static final int PORT = 8883;
    private static final String BROKER = HOST + ":" + PORT;
    private static final String username = "termin8";
    private static final String password = "jeghaterbarnmedraraksent";
    private static final short keepAliveTimer = 300;
    private static final String lastWillTopic = "controller/last_will";
    private static final String lastWillMessage = "Snakkes aldri";
    private MemoryPersistence memoryPersistence = new MemoryPersistence();

    public MQTTClient2(){
        try {
            this.client = new MqttClient(BROKER, "Controller", new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            client.connect(options);
        } catch (MqttException e) {
            //e.printStackTrace();
        }
    }

    public MqttClient getClient(){
        return this.client;
    }

    public static void main(String[] args) throws MqttException {

        MqttClient client = new MQTTClient2().getClient();
        client.subscribe("#");

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(s);
                System.out.println(new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });


    }

}
