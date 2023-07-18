package com.knoldus.function.model;

import com.microsoft.azure.documentdb.Document;

public class VehicleDetails {
    Integer cardId;
    String brand;
    String model;
    Long year;
    String color;
    Double mileage;
    Double price;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(final Integer cardId) {
        this.cardId = cardId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(final Long year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(final Double mileage) {
        this.mileage = mileage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    public VehicleDetails() {
    }

    public VehicleDetails(final Integer cardId, final String brand, final String model, final Long year, final String color, final Double mileage, final Double price) {
        this.cardId = cardId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
    }

    @Override
    public String toString() {
        return "CarDetails{" +
                "cardId=" + cardId +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", mileage=" + mileage +
                ", price=" + price +
                '}';
    }
}
