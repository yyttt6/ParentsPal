package com.example.main.dao.blog.repository

import com.example.main.dao.blog.QnAComment
import org.springframework.data.jpa.repository.JpaRepository

interface QnACommentRepository: JpaRepository<QnAComment, Long> {
    fun findByQnaId(userId: Long): List<QnAComment>
}