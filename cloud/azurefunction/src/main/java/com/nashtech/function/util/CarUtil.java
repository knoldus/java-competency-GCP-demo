package com.nashtech.function.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CarUtil {

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
            String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_lJoEE2SbTTY0Vyo2vAEjfBIZLDGequaveJQDvoej&currencies=INR";

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();

                JSONObject json = new JSONObject(jsonResponse);
                JSONObject data = json.getJSONObject("data");
                double inrValue = data.getDouble("INR");
                priceInRupees = price*inrValue;

            } else {
                System.out.println("HTTP Request failed with response code: " + responseCode);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return priceInRupees;
    }
}
