package com.example.main.service.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Baby;
import com.example.main.dao.login.Parent;
import com.example.main.response.LoginMessage;

import java.util.List;
import java.util.Optional;

public interface ParentService {

    LoginMessage addNewAppUser(ParentDTO parentDTO);

    LoginMessage loginAppUser(LoginDTO loginDTO);

    Optional<Parent> getParentById(Long parentId);
    Baby addBabyToParent(Long parentId, Baby baby);
    Long getParentIdByPhoneNumber(String phoneNumber);

    List<Baby> getBabiesByParentId(Long parentId);

    String getParentNameById(Long parentId);

    LoginMessage changePassword(Long parentId, String oldPassword, String newPassword);

}
