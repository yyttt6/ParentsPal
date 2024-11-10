package com.example.main.dao.blog.repository

import com.example.main.dao.blog.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<Article, Long>{
    fun findByIdUser(id_user: Long): List<Article>
}