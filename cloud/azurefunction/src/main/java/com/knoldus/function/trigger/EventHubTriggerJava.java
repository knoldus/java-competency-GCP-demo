package com.knoldus.function.trigger;

import com.knoldus.function.util.VehicleUtil;
import com.knoldus.function.model.VehicleDetail;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
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
            @EventHubTrigger(name = "message",
                    eventHubName = "myeventhub",
                    connection = "connectionString",
                    consumerGroup = "$Default",
                    cardinality = Cardinality.MANY)
            List<Car> carDetails,
            @CosmosDBOutput(
                    name = "updatedCarDetails",
                    databaseName = "CarFactory",
                    collectionName = "DbContainer",
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
                        details.setCarId(details.getCardId() + 1);
                        return details;
                    }).toList();
            updatedCarDetails.setValue(carDetailsList);
        } catch (Exception exception) {
            context.getLogger().info(exception.getMessage());
        }
        }


}
