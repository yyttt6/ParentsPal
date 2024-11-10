package com.example.main.dao.blog.repository

import com.example.main.dao.blog.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}