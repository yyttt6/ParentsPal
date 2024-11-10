package com.example.main.service.login;

import com.example.main.dto.login.ParentDTO;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dao.login.Baby;
<<<<<<< HEAD
import com.example.main.dao.login.Parent;
=======
import com.example.main.entity.Parent;
>>>>>>> 50e6ab045ddc0ce5bb92c54aec2d72057afde8d9
import com.example.main.response.LoginMessage;

import java.util.Optional;

public interface ParentService {

    String addNewAppUser(ParentDTO parentDTO);

    LoginMessage loginAppUser(LoginDTO loginDTO);

    Optional<Parent> getParentById(Long parentId);
    Baby addBabyToParent(Long parentId, Baby baby);
}
