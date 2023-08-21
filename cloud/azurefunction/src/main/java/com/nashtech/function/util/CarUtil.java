package com.nashtech.function.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
public class CarUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarUtil.class);
    private static String apiUrl = System.getenv("API_URL");

    /**
     * Converts the given mileage value from miles to kilometers per mile.
     *
     * @param mileage the mileage value in miles
     * @return the updated mileage value in kilometers per mile
     */
    public static Double updateMileage(final Double mileage) {
        Double mileageInKmps=mileage*1.6093440006147;
        return mileageInKmps;
    }

    /**
     * Converts the given price value to rupees.
     *
     * @param price the price value
     * @return the updated price value in rupees
     */
    public static Double updatePrice(final Double price) {
        Double priceInRupees=0.0;
        try {


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                StringBuilder response = new StringBuilder();
                try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                String jsonResponse = response.toString();

                JSONObject json = new JSONObject(jsonResponse);
                JSONObject data = json.getJSONObject("data");
                double inrValue = data.getDouble("INR");
                priceInRupees = price*inrValue;

            } else {
                LOGGER.info("HTTP Request failed with response code: " + responseCode);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return priceInRupees;
    }
}