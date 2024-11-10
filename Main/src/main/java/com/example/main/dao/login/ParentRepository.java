package com.example.main.dao.login;

import com.example.main.dao.login.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent,Long> {
    Parent findByPhoneNumber(String phoneNumber);
    Optional<Parent> findOneByPhoneNumberAndPassword(String phoneNumber, String password);
    Optional<Parent> getByName(String name);
}
