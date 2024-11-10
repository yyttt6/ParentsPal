package com.example.LoginRegister.controller;
import com.example.LoginRegister.dto.ImmunizationDTO;
import com.example.LoginRegister.entity.Immunization;
import com.example.LoginRegister.service.ImmunizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/immunizations")
public class ImmunizationController {

    @Autowired
    private ImmunizationService immunizationService;

    @PostMapping("/{babyId}/add")
    public ResponseEntity<Immunization> addImmunization(
            @PathVariable Long babyId,
            @RequestBody ImmunizationDTO immunizationDTO) {
        try {
            Immunization immunization = new Immunization();
            immunization.setBabyId(babyId);  
            immunization.setVaccineName(immunizationDTO.getVaccineName());
            immunization.setDateGiven(immunizationDTO.getDateGiven());
            immunization.setNextDue(immunizationDTO.getNextDue());

            Immunization savedImmunization = immunizationService.addImmunization(immunization);
            return ResponseEntity.ok(savedImmunization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{babyId}")
    public List<Immunization> getImmunizations(@PathVariable Long babyId) {
        return immunizationService.getImmunizations(babyId);
    }

    @PutMapping("/{id}")
    public Immunization updateImmunization(@PathVariable Long id, @RequestBody ImmunizationDTO immunizationDTO) {
        return immunizationService.updateImmunization(id, immunizationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteImmunization(@PathVariable Long id) {
        immunizationService.deleteImmunization(id);
    }
}
