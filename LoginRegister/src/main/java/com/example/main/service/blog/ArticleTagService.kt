package com.example.main.service.blog

import com.example.main.dao.blog.ArticleTag
import com.example.main.dto.blog.ArticleTagDTO

interface ArticleTagService {
    fun getArticleTagById(id: Long): ArticleTag
    fun createArticleTag(articleTagDTO: ArticleTagDTO): Long?
    fun deleteArticleTagById(id: Long)
    fun updateArticleTagById(id: Long, name: String): ArticleTag
}