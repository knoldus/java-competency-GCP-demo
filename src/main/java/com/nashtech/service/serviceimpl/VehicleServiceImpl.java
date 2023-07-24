package com.nashtech.service.serviceimpl;

import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Vehicle;
import com.nashtech.model.VehicleDTO;
import com.nashtech.repository.VehicleRepository;
import com.nashtech.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class VehicleServiceImpl implements VehicleService {

    /**
     * The VehicleRepository instance used to retrieve vehicle information.
     */
    /**
     * The duration of the interval,
     * in seconds, for retrieving unique brand names of vehicles.
     */
    private static final Integer DELAY_TIME = 1;
    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Retrieves a Flux of unique brand names from the vehicle repository.
     *
     * @return A Flux of String representing unique brand names of vehicles.
     * @throws DataNotFoundException
     * If no data is found for any brand name.
     * @throws FirestoreDataException
     * If there is an error while fetching data from the repository.
     */

    @Override
    public Flux<String> getAllBrandNames() {
        try {
            return vehicleRepository.findAll()
                    .filter(vehicle -> vehicle.getBrand() != null)
                    .map(Vehicle::getBrand)
                    .distinct()
                    .delayElements(Duration.ofSeconds(DELAY_TIME));

        } catch (DataNotFoundException dataNotFoundException) {
            throw  new DataNotFoundException(
                    "Data Not Found");
        } catch (FirestoreException firestoreException) {
            throw new FirestoreDataException(
                    firestoreException.getMessage(),
                    firestoreException
            );
        }

    }

    /**
     * Retrieves vehicle details by the specified brand name.
     *
     * @param brand The brand name of the vehicles to retrieve details for.
     * @return A Flux of VehicleDTO representing
     * the details of vehicles with the given brand name.
     * @throws DataNotFoundException
     * If no data is found for the specified brand name.
     * @throws FirestoreDataException
     * If there is an error while fetching data from the repository.
     */
    @Override
    public Flux<VehicleDTO> getDetailsByBrandName(final String brand)  {
        try
        {
         return  vehicleRepository.findByBrand(brand)
            .filter(vehicle -> vehicle != null)
            .map(vehicle ->  new VehicleDTO(
                    vehicle.getCarId(),
                    vehicle.getCarModel(),
                    vehicle.getBrand(),
                    vehicle.getYear(),
                    vehicle.getColor(),
                    vehicle.getMileage(),
                    vehicle.getPrice()))
                 .distinct()
                 .delayElements(Duration.ofSeconds(DELAY_TIME));
        }
        catch (DataNotFoundException dataNotFoundException){
            throw  new DataNotFoundException("Data not found");
        }
        catch (FirestoreException firestoreException){
            throw new FirestoreDataException(
                    firestoreException.getMessage(),
                    firestoreException
            );
        }


    }

}
