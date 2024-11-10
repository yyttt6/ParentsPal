package com.example.main.dao.conv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByName(String name);
    @Query("SELECT u.name FROM User u WHERE u.user_id = :user_id")
    Optional<String> findNameByUserId(@Param("user_id") Integer userId);
}