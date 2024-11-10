package com.example.main.dto.login;

import java.time.LocalDate;

public class GrowthTrackingDTO {
    private Double weight;
    private Double height;
    private LocalDate measurementDate;

    // Getters and Setters
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public LocalDate getMeasurementDate() { return measurementDate; }
    public void setMeasurementDate(LocalDate measurementDate) { this.measurementDate = measurementDate; }
}