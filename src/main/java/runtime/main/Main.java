package runtime.main;

import data.dao.PlantDAO;
import data.dao.SensorHistoryDAO;
import data.dao.WateringHistoryDAO;
import data.db.DB;
import data.models.Plant;
import data.models.PlantType;
import data.models.SensorHistory;
import data.models.WateringHistory;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import runtime.mqtt.MQTTClient;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {

    private final static String TOPIC_PREFIX = "controller";
    private final static String TOPIC = TOPIC_PREFIX + "/#";
    private final static boolean DEBUG = true;

    private static void debugPrint(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }


    private static BlockingConnection establishMQTTConnection() {
        BlockingConnection connection;
        try {
            connection = MQTTClient.getConnection();
            Topic[] topics = {new Topic(TOPIC, QoS.AT_LEAST_ONCE)};
            byte[] response = connection.subscribe(topics);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            debugPrint("Something went wrong while trying to establish an MQTT-connection and subscribing" +
                    " to the wanted topics.");
            return null;
        }
    }

    private static void updatePlant(Plant plant){
        plant.setLast_watered(new Timestamp(System.currentTimeMillis()));
        new PlantDAO().update(plant);

        SensorHistory lastHistory = new SensorHistoryDAO().getLastByPlantId(plant.getId());
        WateringHistory wateringHistory = new WateringHistory(9999,lastHistory.getTemp(),
                lastHistory.getMoisture(),
                new Timestamp(System.currentTimeMillis()),
                plant.getId());

        new WateringHistoryDAO().create(wateringHistory);
    }

    private static void checkMQTT(BlockingConnection connection) {
        Message message;
        try {
            while ( (message = connection.receive(1, TimeUnit.SECONDS)) != null) {

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
                        } else {
                            debugPrint("Received a message about a plant which does not exist.. The topic was: " + message.getTopic());
                        }
                        message.ack();
                    } else {
                        debugPrint("This should not happen... received a message from a topic which we are not subscribed to.");
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

    private static void checkDB(Connection connection, BlockingConnection MQTTConnection) {

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
                if (plant.isAutomatic_water()) {
                    //Water that shit!
                    debugPrint("Plant with id " + plant.getId() + " needs watering. Sending message to RPi.");
                    try {
                        MQTTConnection.publish("water/" + plant.getId(),
                                "start".getBytes(),
                                QoS.EXACTLY_ONCE,
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
                                QoS.AT_LEAST_ONCE,
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


    public static void main(String[] args) {
        /*
        Main-function which will be an infinite loop
        going over database-data, checking for constraints,
        checking MQTT if there has been a request for watering,
        on a regular schedule.
         */

        BlockingConnection MQTTconnection = establishMQTTConnection();
        Connection DBConnection = DB.getConnection();

        while (true) {

            debugPrint("Checking MQTT connection..");
            if (MQTTconnection == null) {
                MQTTconnection = establishMQTTConnection();
            }

            if (MQTTconnection != null) {
                debugPrint("MQTT connection OK; checking for messages to subscribed topics.");
                checkMQTT(MQTTconnection);
            } else {
                debugPrint("Could not establish a connection to the MQTT-broker - " +
                        "function returned null while establishing a connection.");
            }

            debugPrint("Checking DB connection..");
            if (DBConnection == null) {
                DBConnection = DB.getConnection();
            }

            if (DBConnection != null && MQTTconnection != null) {
                debugPrint("DB connection OK; checking for constraints.");
                checkDB(DBConnection, MQTTconnection);
            } else {
                debugPrint("Could not establish a connection with the database server - function returned null" +
                        " while establishing a connection.");
            }

            //Sleep for 1s so as not to kill the poor RPi.
            debugPrint("Sleeping for 1s.");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                debugPrint("Could not sleep. Insomnia sucks, amirite?");
            }
        }
    }
}
