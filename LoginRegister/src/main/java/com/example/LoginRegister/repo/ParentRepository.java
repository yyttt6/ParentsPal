package com.example.LoginRegister.repo;

import com.example.LoginRegister.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent,Long> {
    Parent findByPhoneNumber(String phoneNumber);

    Optional<Parent> findOneByPhoneNumberAndPassword(String phoneNumber, String password);
}
