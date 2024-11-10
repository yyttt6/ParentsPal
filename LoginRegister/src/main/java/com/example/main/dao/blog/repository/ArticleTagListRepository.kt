package com.example.main.dao.blog.repository

import com.example.main.dao.blog.ArticleTag
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTagListRepository: JpaRepository<ArticleTag, Long> {
}