package com.example.main.service.login;

import com.example.main.dto.login.ImmunizationDTO;
import com.example.main.dao.login.Immunization;
import com.example.main.dao.login.ImmunizationRepository;
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
