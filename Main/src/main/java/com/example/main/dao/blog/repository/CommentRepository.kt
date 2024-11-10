package com.example.demo.dao.repository

import com.example.demo.dao.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
}