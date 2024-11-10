package com.example.demo.dao.repository

import com.example.demo.dao.ArticleTag
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTagRepository: JpaRepository<ArticleTag, Long> {
    fun findByName(name: String): ArticleTag?
}