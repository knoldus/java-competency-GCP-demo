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
            List<VehicleDetail> vehicleDetails,
            @CosmosDBOutput(
                    name = "updatedCarDetails",
                    databaseName = "CarFactory",
                    collectionName = "DbContainer",
                    connectionStringSetting = "ConnectionStringSetting",
                    createIfNotExists = true
            )
            OutputBinding<List<VehicleDetail>> updatedVehicleDetails,
            final ExecutionContext context
    ) {
            List<VehicleDetail> vehicleDetailsList = new ArrayList<>();
            vehicleDetailsList = vehicleDetails.stream()
                .map(details -> {
                    context.getLogger().info("Raw Data: " + details);
                    Double updatedMileage = VehicleUtil.updateMileage(details.getMileage());
                    Double updatedPrice = VehicleUtil.updatePrice(details.getPrice());
                    details.setMileage(updatedMileage);
                    details.setPrice(updatedPrice);
                    context.getLogger().info("Transformed Data: " + details);
                    details.setCardId(details.getCardId() + 1);
                    return details;
                }).toList();

        updatedVehicleDetails.setValue(vehicleDetailsList);

        }


}
