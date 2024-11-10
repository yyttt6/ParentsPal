package com.example.demo.dao.repository

import com.example.demo.dao.ArticleTag
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTagListRepository: JpaRepository<ArticleTag, Long> {
}