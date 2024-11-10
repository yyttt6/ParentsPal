package com.example.demo.dao.repository

import com.example.demo.dao.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}