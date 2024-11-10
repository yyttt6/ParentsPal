package com.example.main.dto;

import java.time.LocalDate;

public class ImmunizationDTO {
    private String vaccineName;
    private LocalDate dateGiven;
    private LocalDate nextDue;

    // Constructors
    public ImmunizationDTO() {}

    public ImmunizationDTO(String vaccineName, LocalDate dateGiven, LocalDate nextDue) {
        this.vaccineName = vaccineName;
        this.dateGiven = dateGiven;
        this.nextDue = nextDue;
    }

    // Getters and Setters
    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDate getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven = dateGiven;
    }

    public LocalDate getNextDue() {
        return nextDue;
    }

    public void setNextDue(LocalDate nextDue) {
        this.nextDue = nextDue;
    }
}
