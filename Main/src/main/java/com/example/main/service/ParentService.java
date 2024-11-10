package com.example.main.service;

import com.example.main.dto.ParentDTO;
import com.example.main.dto.LoginDTO;
import com.example.main.entity.Baby;
import com.example.main.entity.Parent;
import com.example.main.response.LoginMessage;

import java.util.Optional;

public interface ParentService {

    String addNewAppUser(ParentDTO parentDTO);

    LoginMessage loginAppUser(LoginDTO loginDTO);

    Optional<Parent> getParentById(Long parentId);
    Baby addBabyToParent(Long parentId, Baby baby);
}
