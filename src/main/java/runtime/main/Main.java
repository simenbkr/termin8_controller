package runtime.main;

import data.dao.PlantDAO;
import data.dao.SensorHistoryDAO;
import data.dao.WateringHistoryDAO;
import data.db.DB;
import data.models.Plant;
import data.models.PlantType;
import data.models.SensorHistory;
import data.models.WateringHistory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.concurrent.CountDownLatch;
import runtime.mqtt.MQTTClient2;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {

    private final static String TOPIC_PREFIX = "controller";
    private final static String TOPIC = TOPIC_PREFIX + "/#";
    private final static String DATA_PREFIX = "data";
    private final static String DATA_TOPIC = DATA_PREFIX + "/#";
    private final static long REFRESH_TIME = 1000 * 3600 * 10;
    private final static boolean DEBUG = true;
    private final static CountDownLatch latch = new CountDownLatch(10);

    private static void debugPrint(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }


    /*
    private static BlockingConnection establishMQTTConnection() {
        BlockingConnection connection;
        try {
            connection = MQTTClient.getConnection();
            Topic[] topics = {new Topic(TOPIC, QoS.AT_LEAST_ONCE), new Topic(DATA_TOPIC, QoS.AT_LEAST_ONCE)};
            byte[] response = connection.subscribe(topics);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            debugPrint("Something went wrong while trying to establish an MQTT-connection and subscribing" +
                    " to the wanted topics.");
            return null;
        }
    }
    */

    private static MqttClient establishMQTTConnection(){
        MqttClient client;

        try {

            client = new MQTTClient2().getClient();

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    debugPrint("Lost connection to broker..");
                    latch.countDown();
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

                    debugPrint("Recieved a message:\n Topic: " + topic + "\n Message: " + new String(mqttMessage.getPayload()));

                    if(topic.startsWith(TOPIC_PREFIX)){
                        int plant_id = Integer.valueOf(topic.split("/")[1]);
                        Plant plant = new PlantDAO().getByID(plant_id);
                        String[] content = new String(mqttMessage.getPayload()).split(":");
                        if (plant != null){
                            int time;
                            try {
                                time = Integer.valueOf(content[1]);
                            } catch(Exception e){
                                time = 1;
                            }

                            updatePlant(plant);
                            String payload = "start:time:" + time;
                            client.publish("water/" + plant_id,
                                    payload.getBytes(),
                                    2,
                                    false);

                        }
                    } else if(topic.startsWith(DATA_PREFIX)){
                        String[] topicParts = topic.split("/");
                        int plant_id = Integer.valueOf(topicParts[0]);
                        Plant plant = new PlantDAO().getByID(plant_id);

                        if(plant != null) {
                            String[] content = new String(mqttMessage.getPayload()).split(":");
                            int secs = Integer.valueOf(content[0]);
                            Timestamp timestamp = new Timestamp(secs);
                            float temp = Float.valueOf(content[1]);
                            float moisture = Float.valueOf(content[2]);

                            SensorHistory history = new SensorHistory(80085, temp, moisture, plant_id, timestamp);
                            new SensorHistoryDAO().create(history);
                        }
                    }

                    latch.countDown();


                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    latch.countDown();
                }
            });
            client.subscribe(TOPIC);
            client.subscribe(DATA_TOPIC);
            client.setTimeToWait((long) 50);

        } catch (Exception e){
            //e.printstacktrace()asdasd;
            return null;
        }
        return client;
    }

    private static void updatePlant(Plant plant){
        if(plant == null){
            return;
        }

        plant.setLast_watered(new Timestamp(System.currentTimeMillis()));
        new PlantDAO().update(plant);

        SensorHistory lastHistory = new SensorHistoryDAO().getLastByPlantId(plant.getId());

        if (lastHistory != null) {

            WateringHistory wateringHistory = new WateringHistory(9999, lastHistory.getTemp(),
                    lastHistory.getMoisture(),
                    new Timestamp(System.currentTimeMillis()),
                    plant.getId());

            new WateringHistoryDAO().create(wateringHistory);
        }
    }

    /*private static void checkMQTT(BlockingConnection connection) {
        Message message;
        try {
            //while ( (message = connection.receive()) != null) {
            while (true) {
                message = connection.receive(1,TimeUnit.MILLISECONDS);
                if (message == null)
                    break;

                debugPrint(message.toString());
                try {
                    if (message.getTopic().startsWith(TOPIC_PREFIX)) {
                        String[] topicParts = message.getTopic().split("/");
                        int plant_id = Integer.valueOf(topicParts[1]);
                        String[] content = (new String(message.getPayload())).split(":");
                        debugPrint("Topic: " + message.getTopic() + "\n");
                        debugPrint("Message: " + new String(message.getPayload()) + "\n");
                        int time;
                        try {
                            time = Integer.valueOf(content[1]);
                        } catch(Exception e){
                            time = 1;
                        }

                        Plant plant = new PlantDAO().getByID(plant_id);
                        if(plant != null) {
                            updatePlant(plant);
                            String payload = "start:" + "time:" + time;
                            connection.publish("water/" + plant.getId(),
                                    payload.getBytes(),
                                    QoS.EXACTLY_ONCE,
                                    false);

                            debugPrint("Received a message! Topic: " + message.getTopic());
                        }
                        else {
                            debugPrint("Received a message about a plant which does not exist.. The topic was: " + message.getTopic());
                        }
                        message.ack();
                    }
                    else if(message.getTopic().startsWith(DATA_PREFIX)){
                    */
                        /*
                        Expected format:
                        Time is in seconds since epoch, temp and moisture as floating point numbers.
                        data is posted to data/<plantid> as time:temp:moisture
                         */
                        /*

                        String[] topicParts = message.getTopic().split("/");
                        int plant_id = Integer.valueOf(topicParts[0]);
                        Plant plant = new PlantDAO().getByID(plant_id);
                        if (plant != null) {
                            String[] content = (new String(message.getPayload())).split(":");
                            int secs = Integer.valueOf(content[0]);
                            Timestamp timestamp = new Timestamp(secs);
                            float temp = Float.valueOf(content[1]);
                            float moisture = Float.valueOf(content[2]);

                            SensorHistory history = new SensorHistory(80085, temp, moisture, plant_id, timestamp);
                            new SensorHistoryDAO().create(history);

                            debugPrint("Created a new sensorhistory for plant with id " + plant_id + ".");
                        } else {
                            debugPrint("Plant with id " + plant_id + " does not seem to exists.. Skipping data-add.");
                        }

                    }
                    else {
                        debugPrint("This should not happen... received a message from a topic which we are not subscribed to.");
                        message.ack();
                    }
                } catch(Exception e){
                    e.printStackTrace();
                    debugPrint("Something went wrong trying to checkMQTT.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            debugPrint("Something went wrong trying to receive, and/or process MQTT-messages");
        }
    }
    */

    private static void checkDB(Connection connection, MqttClient MQTTConnection) {

        if (connection == null || MQTTConnection == null) {
            return;
        }

        PlantDAO plantDAO = new PlantDAO();
        ArrayList<Plant> plants = (ArrayList<Plant>) plantDAO.listAll();

        for (Plant plant : plants) {

            SensorHistory lastHistory = new SensorHistoryDAO().getLastByPlantId(plant.getId());
            PlantType type = plant.getPlantType();
            if (lastHistory == null || type == null) {
                debugPrint("Lasthistory or PlantType was null. Skipping..");
                continue;
            }
            if (lastHistory.getMoisture() < type.getMin_moisture()) {
                //Needs watering
                Timestamp timestamp = plant.getLast_watered();
                long millis = timestamp.getTime();
                //Gotta get that timer so we dont overwater that shit.

                //debugPrint(System.currentTimeMillis() - millis + "");
                if (plant.isAutomatic_water() && Math.abs(System.currentTimeMillis() - millis) > (long) 600*100) {
                    //Water that shit!
                    debugPrint("Plant with id " + plant.getId() + " needs watering. Sending message to RPi.");
                    try {
                        MQTTConnection.publish("water/" + plant.getId(),
                                "start:time:1".getBytes(),
                                2,
                                false);

                        updatePlant(plant);

                    } catch (Exception e) {
                        e.printStackTrace();
                        debugPrint("Something went wrong trying to publish a start message.");
                    }
                } else {
                    //Notify user about that shit!
                    //This is done through Django/webapp though, right?
                }

            } else if (lastHistory.getMoisture() > type.getMax_moisture()) {
                //Watered too much, aaaaa!
                if (plant.isAutomatic_water()) {
                    //STOOOOPPPPOOP
                    debugPrint("Plant with id " + plant.getId() + " has been overwatered. Sending message to stop watering.");
                    try {
                        MQTTConnection.publish("water/" + plant.getId(),
                                "stop".getBytes(),
                                2,
                                false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        debugPrint("Something went wrong while trying to publish a stop message.");
                    }
                }
                //And notify!
            } else {
                //We gewd.
            }

            if (lastHistory.getTemp() < type.getMin_temp()) {
                //too cold for the plant man. Not good.
            } else if (lastHistory.getTemp() > type.getMax_temp()) {
                //too hot man
            } else {
                //is gewd.
            }
        }


    }


    public static void main(String[] args) throws InterruptedException {
        /*
        Main-function which will be an infinite loop
        going over database-data, checking for constraints,
        checking MQTT if there has been a request for watering,
        on a regular schedule.
         */

        //BlockingConnection MQTTconnection = establishMQTTConnection();
        MqttClient client = establishMQTTConnection();
        Connection DBConnection = DB.getConnection();

        while (true) {

            debugPrint("Checking MQTT connection..");
            if (client == null || !client.isConnected()) {
                client = establishMQTTConnection();
            }

            //if (client != null)
                //client


            debugPrint("Checking DB connection..");
            if (DBConnection == null) {
                DBConnection = DB.getConnection();
            }

            if (DBConnection != null && client != null) {
                debugPrint("DB connection OK; checking for constraints.");
                checkDB(DBConnection, client);
            } else {
                debugPrint("Could not establish a connection with the database server - function returned null" +
                        " while establishing a connection.");
            }

            //Sleep for 1s so as not to kill the poor RPi.
            debugPrint("Sleeping for 1s.");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                debugPrint("Could not sleep. Insomnia sucks, amirite?");
            }
        }
    }
}
