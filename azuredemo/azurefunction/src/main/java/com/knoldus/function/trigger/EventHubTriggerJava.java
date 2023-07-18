package com.knoldus.function.trigger;

import com.knoldus.function.data.TransformData;
import com.knoldus.function.model.VehicleDetails;
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
            List<VehicleDetails> vehicleDetails,
            @CosmosDBOutput(
                    name = "updatedCarDetails",
                    databaseName = "CarFactory",
                    collectionName = "DbContainer",
                    connectionStringSetting = "ConnectionStringSetting",
                    createIfNotExists = true
            )
            OutputBinding<List<VehicleDetails>> updatedVehicleDetails,
            final ExecutionContext context
    ) {
            List<VehicleDetails> vehicleDetailsList = new ArrayList<>();
            for(VehicleDetails details: vehicleDetails){
                context.getLogger().info("Raw Data : " + details);
                Double updatedMileage = TransformData.updateMileage(details.getMileage());
                Double updatedPrice = TransformData.updatePrice(details.getPrice());
                details.setMileage(updatedMileage);
                details.setPrice(updatedPrice);
                context.getLogger().info("Transformed Data : " + details);
                details.setCardId(details.getCardId()+1);
               vehicleDetailsList.add(details);
            }
        updatedVehicleDetails.setValue(vehicleDetailsList);

        }


}
