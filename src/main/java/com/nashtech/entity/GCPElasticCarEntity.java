package com.nashtech.entity;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "car-details")
public class GCPElasticCarEntity {

    @Id
    private String id;

    @Field(type = FieldType.Integer, name = "carId")
    @Unique
    private Integer carId;

    @Field(type = FieldType.Text, name = "model")
    private String model;

    @Field(type = FieldType.Text, name = "brand")
    private String brand;

    @Field(type = FieldType.Long, name = "year")
    private Long year;

    @Field(type = FieldType.Text, name = "color")
    private String color;

    @Field(type = FieldType.Double, name = "name")
    private Double mileage;

    @Field(type = FieldType.Double, name = "name")
    private Double price;
}
