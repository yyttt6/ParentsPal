package com.example.main.service.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.response.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.main.dao.login.Baby;
import com.example.main.dao.login.BabyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private BabyRepository babyRepository;

    @Override
    public String addNewAppUser (ParentDTO parentDTO) {

        if (parentRepository.existsByPhoneNumber(parentDTO.getPhoneNumber())) {
            return "Phone number already exists";
        }

        Parent parent = new Parent(
                parentDTO.getId(),
                parentDTO.getName(),
                parentDTO.getPhoneNumber(),
                parentDTO.getPassword()
        );
        parentRepository.save(parent);
        return "Registration successful";
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
                    Parent loggedInParent = employee.get();
                    return new LoginMessage("Login Success. Parent ID: " + loggedInParent.getId(), true);
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

    @Override
    public Long getParentIdByPhoneNumber(String phoneNumber) {
        Parent parent = parentRepository.findByPhoneNumber(phoneNumber);
        return parent != null ? parent.getId() : null;
    }

    @Override
    public List<Baby> getBabiesByParentId(Long parentId) {
        Optional<Parent> optionalParent = parentRepository.findById(parentId);
        if (optionalParent.isPresent()) {
            Parent parent = optionalParent.get();
            return parent.getBabies();
        }
        return new ArrayList<>();
    }

}
