package com.example.LoginRegister.service;

import com.example.LoginRegister.dto.ImmunizationDTO;
import com.example.LoginRegister.entity.Immunization;
import com.example.LoginRegister.repo.ImmunizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ImmunizationService {
    @Autowired
    private ImmunizationRepository immunizationRepository;

    public Immunization addImmunization(Immunization immunization) {
        return immunizationRepository.save(immunization);
    }

    public List<Immunization> getImmunizations(Long babyId) {
        return immunizationRepository.findByBabyId(babyId);
    }

    public Immunization updateImmunization(Long id, ImmunizationDTO immunizationDetails) {
        Immunization immunization = immunizationRepository.findById(id).orElseThrow();
        immunization.setVaccineName(immunizationDetails.getVaccineName());
        immunization.setDateGiven(immunizationDetails.getDateGiven());
        immunization.setNextDue(immunizationDetails.getNextDue());
        return immunizationRepository.save(immunization);
    }

    public void deleteImmunization(Long id) {
        immunizationRepository.deleteById(id);
    }
}
