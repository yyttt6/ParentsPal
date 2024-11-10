package com.example.demo.service

import com.example.demo.dao.ArticleTag
import com.example.demo.dto.ArticleTagDTO

interface ArticleTagService {
    fun getArticleTagById(id: Long): ArticleTag
    fun createArticleTag(articleTagDTO: ArticleTagDTO): Long?
    fun deleteArticleTagById(id: Long)
    fun updateArticleTagById(id: Long, name: String): ArticleTag
}