package com.nashtech.function.trigger;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.nashtech.function.util.CarUtil;
import com.nashtech.function.model.Car;
import java.util.*;

/**
 * Azure Functions with Event Hub trigger.
 */
public class EventHubTriggerJava {


    /**
     * This function will be invoked when an event is received from Event Hub.
     */
    @FunctionName("EventHubTriggerJava")
    public void run(
            @EventHubTrigger(name = "carDetails",
                    eventHubName = "eventhub",
                    connection = "connectionString",
                    consumerGroup = "$Default",
                    cardinality = Cardinality.MANY)
            List<Car> carDetails,
            @CosmosDBOutput(
                    name = "updatedCarDetails",
                    databaseName = "az-nashtech-db",
                    collectionName = "az-car-collection",
                    connectionStringSetting = "ConnectionStringSetting",
                    createIfNotExists = true
            )
            OutputBinding<List<Car>> updatedCarDetails,
            final ExecutionContext context
    ) {
        try {
            List<Car> carDetailsList = new ArrayList<>();
            carDetailsList = carDetails.stream()
                    .map(details -> {
                        context.getLogger().info("Car Data: " + details);
                        Double updatedMileage = CarUtil.updateMileage(details.getMileage());
                        Double updatedPrice = CarUtil.updatePrice(details.getPrice());
                        details.setMileage(updatedMileage);
                        details.setPrice(updatedPrice);
                        context.getLogger().info("Transformed Car Data: " + details);
                        details.setCarId(details.getCarId());
                        return details;
                    }).toList();
            updatedCarDetails.setValue(carDetailsList);
        } catch (Exception exception) {
            context.getLogger().info(exception.getMessage());
        }
    }
}