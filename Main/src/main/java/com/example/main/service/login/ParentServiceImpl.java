package com.example.main.service.login;
import com.example.main.service.encry.EncryptionService;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.response.LoginMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.example.main.dao.login.Baby;
import com.example.main.dao.login.BabyRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private BabyRepository babyRepository;

    @Autowired
    private final EncryptionService encryptionService;

    public ParentServiceImpl(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public LoginMessage addNewAppUser (ParentDTO parentDTO) {

        if (parentRepository.existsByPhoneNumber(parentDTO.getPhoneNumber())) {
            return new LoginMessage("Phone number already exists", false);
        }



        Parent parent = new Parent(
                parentDTO.getId(),
                parentDTO.getName(),
                parentDTO.getPhoneNumber(),
                encryptionService.encrypt(parentDTO.getPassword())
        );
        Parent savedParent = parentRepository.save(parent);

        LoginMessage loginMessage = new LoginMessage("Registration successful", true);
        loginMessage.setParentId(savedParent.getId());
        loginMessage.setParentName(savedParent.getName());
        return loginMessage;
    }
    @Override
    public LoginMessage loginAppUser(LoginDTO loginDTO) {
        String msg = "";
        Parent parent1 = parentRepository.findByPhoneNumber(loginDTO.getPhoneNumber());
        if (parent1 != null) {
            String password = loginDTO.getPassword();
            String storedPassword = encryptionService.decrypt(parent1.getPassword());
            Boolean isPwdRight = password.equals(storedPassword);
            if (isPwdRight) {
                Optional<Parent> employee = parentRepository.findOneByPhoneNumberAndPassword(loginDTO.getPhoneNumber(), parent1.getPassword());

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

    @Override
    public String getParentNameById(Long parentId) {
        Optional<Parent> parentOpt = parentRepository.findById(parentId);
        return parentOpt.map(Parent::getName).orElse(null);
    }
    @Override
    public LoginMessage changePassword(Long parentId, String oldPassword, String newPassword) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
    
        if (!encryptionService.decrypt(parent.getPassword()).equals(oldPassword)) {
            return new LoginMessage( "The original password is wrong.",false);
        }
    
        if (newPassword.equals(oldPassword)) {
            return new LoginMessage( "Cannot use previous password",false);
        }
    
        if (newPassword == null || newPassword.isEmpty()) {
            return new LoginMessage( "New password cannot be empty",false);
        }
    
        parent.setPassword(encryptionService.encrypt(newPassword));
        parentRepository.save(parent);
        return new LoginMessage( "Password changed successfully",true);
    }
    @Override
    public Parent uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        Optional<Parent> optionalParent = parentRepository.findById(id);
        if (optionalParent.isPresent()) {
            Parent parent = optionalParent.get();
            parent.setProfilePicture(file.getBytes());
            return parentRepository.save(parent);
        } else {
            throw new RuntimeException("Parent not found with id: " + id);
        }
    }
    @Override
    public byte[] getProfilePicture(Long id) {
        return parentRepository.findById(id)
                .map(Parent::getProfilePicture)
                .orElseThrow(() -> new RuntimeException("Profile picture was not found for id: " + id));
    }

    @Override
    public LoginMessage changeName(Long parentId, String newName) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));

        parent.setName(newName);
        parentRepository.save(parent);

        return new LoginMessage( "Name updated successfully.",true);
    }
    
    @Override
    public LoginMessage changePhone(Long parentId, String newPhone) {
        // Check if the phone number is already taken
        if (parentRepository.existsByPhoneNumber(newPhone)) {
            return new LoginMessage( "Phone number already in use.",false);
        }

        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));

        parent.setPhoneNumber(newPhone);
        parentRepository.save(parent);

        return new LoginMessage("Phone number updated successfully.",true);
    }

    public void setParentRepository(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    public void setBabyRepository(BabyRepository babyRepository) {
        this.babyRepository = babyRepository;
    }
    @Override
    public void uploadExpertPicture(Long id, MultipartFile file) throws IOException {
        Optional<Parent> parentOptional = parentRepository.findById(id);

        if (parentOptional.isEmpty()) {
            throw new IllegalArgumentException("Parent not found.");
        }

        Parent parent = parentOptional.get();
        parent.setExpertPicture(file.getBytes());
        parent.setExpert(true);
        parentRepository.save(parent);
    }
    @Override
    public byte[] getExpertPicture(Long id) {
        Optional<Parent> parentOptional = parentRepository.findById(id);

        if (parentOptional.isEmpty() || parentOptional.get().getExpertPicture() == null) {
            throw new IllegalArgumentException("Expert picture not found.");
        }

        return parentOptional.get().getExpertPicture();
    }
    @Override
    public void setExpertStatus(Long id, boolean isExpert) {
        Optional<Parent> parentOptional = parentRepository.findById(id);

        if (parentOptional.isEmpty()) {
            throw new IllegalArgumentException("Parent not found.");
        }

        Parent parent = parentOptional.get();
        parent.setExpert(isExpert);
        parentRepository.save(parent);
    }
    @Override
    public boolean getExpertStatus(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + id));
        return parent.isExpert();
    }
}
