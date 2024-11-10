package com.example.demo.dao.repository

import com.example.demo.dao.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<Article, Long>{
    fun findByIdUser(id_user: Long): List<Article>
}