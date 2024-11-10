package com.example.main.service.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Baby;
import com.example.main.entity.Parent;
import com.example.main.response.LoginMessage;

import java.util.Optional;

public interface ParentService {

    String addNewAppUser(ParentDTO parentDTO);

    LoginMessage loginAppUser(LoginDTO loginDTO);

    Optional<Parent> getParentById(Long parentId);
    Baby addBabyToParent(Long parentId, Baby baby);
}
