package com.example.LoginRegister.service;

import com.example.LoginRegister.dto.ParentDTO;
import com.example.LoginRegister.dto.LoginDTO;
import com.example.LoginRegister.entity.Baby;
import com.example.LoginRegister.entity.Parent;
import com.example.LoginRegister.response.LoginMessage;

import java.util.Optional;

public interface ParentService {

    String addNewAppUser(ParentDTO parentDTO);

    LoginMessage loginAppUser(LoginDTO loginDTO);

    Optional<Parent> getParentById(Long parentId);
    Baby addBabyToParent(Long parentId, Baby baby);
}
