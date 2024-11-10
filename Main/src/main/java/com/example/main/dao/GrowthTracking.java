package com.example.LoginRegister.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "growth_tracking")
public class GrowthTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double weight; // in kg
    private Double height; // in cm
    private LocalDate measurementDate;

    @ManyToOne
    @JoinColumn(name = "baby_id", nullable = false)
    private Baby baby;

    public GrowthTracking() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public LocalDate getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDate measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Baby getBaby() {
        return baby;
    }

    public GrowthTracking(Double weight, Double height, LocalDate measurementDate, Baby baby) {
        this.weight = weight;
        this.height = height;
        this.measurementDate = measurementDate;
        this.baby = baby;
    }

}
