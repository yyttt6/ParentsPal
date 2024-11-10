package com.example.main.dao.blog.repository

import com.example.main.dao.blog.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
}