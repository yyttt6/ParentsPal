package com.example.LoginRegister.service;

import com.example.LoginRegister.dto.ParentDTO;
import com.example.LoginRegister.dto.LoginDTO;
import com.example.LoginRegister.entity.Parent;
import com.example.LoginRegister.repo.ParentRepository;
import com.example.LoginRegister.response.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.LoginRegister.entity.Baby;
import com.example.LoginRegister.entity.Baby;
import com.example.LoginRegister.repo.BabyRepository;

import java.util.ArrayList;
import java.util.Optional;
@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private BabyRepository babyRepository;

    @Override
    public String addNewAppUser (ParentDTO parentDTO) {
        Parent parent = new Parent(
                parentDTO.getId(),
                parentDTO.getName(),
                parentDTO.getPhoneNumber(),
                parentDTO.getPassword()
        );
        parentRepository.save(parent);
        return parent.getName();
    }
    @Override
    public LoginMessage loginAppUser(LoginDTO loginDTO) {
        String msg = "";
        Parent parent1 = parentRepository.findByPhoneNumber(loginDTO.getPhoneNumber());
        if (parent1 != null) {
            String password = loginDTO.getPassword();
            String storedPassword = parent1.getPassword();
            Boolean isPwdRight = password.equals(storedPassword);
            if (isPwdRight) {
                Optional<Parent> employee = parentRepository.findOneByPhoneNumberAndPassword(loginDTO.getPhoneNumber(), storedPassword);
                if (employee.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("password Not Match", false);
            }
        }else {
            return new LoginMessage("Phone number not exits", false);
        }
    }

    @Override
    public Optional<Parent> getParentById(Long parentId) {
        return parentRepository.findById(parentId);
    }

    @Override
    public Baby addBabyToParent(Long parentId, Baby baby) {
        Optional<Parent> optionalParent = getParentById(parentId);

        if (optionalParent.isPresent()) {
            Parent parent = optionalParent.get();
            baby.setParent(parent);
            Baby addedBaby = babyRepository.save(baby);
            parent.getBabies().add(addedBaby);
            parentRepository.save(parent);
            return addedBaby;
        } else {
            return null;
        }
    }
}
