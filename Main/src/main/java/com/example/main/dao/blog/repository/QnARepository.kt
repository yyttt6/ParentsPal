package com.example.main.dao.blog.repository

import com.example.main.dao.blog.QnA
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QnARepository: JpaRepository<QnA, Long> {
    fun findByUserId(user_id: Long): List<QnA>
}

