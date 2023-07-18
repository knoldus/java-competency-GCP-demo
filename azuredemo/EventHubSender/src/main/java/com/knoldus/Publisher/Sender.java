package com.knoldus.Publisher;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.knoldus.model.VehicleDetails;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sender {

    private static final String connectionString = "Endpoint=sb://mynamespace2023125.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=waqRW0PFooNb9rTAyA5uoLRiHzc9IPsyH+AEhLQX/wk=";
    private static final String eventHubName = "myeventhub";

    /**
     * Publishes events to the Azure Event Hub.
     */
    public static void publishEvents() {
        /**
         *create a producer client.
         */
        EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .buildProducerClient();

        List<VehicleDetails> allVehicleData = Arrays.asList(
                new VehicleDetails(1, "Toyota", "Camry", 2018, "White", 45.0, 18000.0),
                new VehicleDetails(2, "Maruti Suzuki", "Swift", 2018, "White", 55.0, 18000.0)
        );
        /**
         *sample events in an array.
         */
        List<EventData> allEvents = new ArrayList<>();

        for (VehicleDetails details : allVehicleData) {
            /**
             *Convert UserData to JSON or any other suitable format for sending.
             */
            JSONObject jsonVehicleObject = new JSONObject(details);

            /**
             *Create an EventData instance with the serialized user data.
             */
            EventData eventData = new EventData(String.valueOf(jsonVehicleObject));
            /**
             *Add the event to the list of events.
             */
            allEvents.add(eventData);
        }
        /**
         *create a batch.
         */
        EventDataBatch eventDataBatch = producer.createBatch();

        for (EventData eventData : allEvents) {
            if (!eventDataBatch.tryAdd(eventData)) {
                /**
                 *if the batch is full, send it and then create a new batch.
                 */
                producer.send(eventDataBatch);
                eventDataBatch = producer.createBatch();

                if (!eventDataBatch.tryAdd(eventData)) {
                    throw new IllegalArgumentException("Event is too large for an empty batch. Max size: "
                            + eventDataBatch.getMaxSizeInBytes());
                }
            }
        }
        /**
         *send the last batch of remaining events
         */
        if (eventDataBatch.getCount() > 0) {
            producer.send(eventDataBatch);
        }
        producer.close();
    }
}
