package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.Article

interface ArticleService {
    fun getArticleById(id: Long): Response<Article>
    fun getArticleByUserId(userId: Long): List<Article>
    fun createArticle(article: Article): Long?
    fun deleteArticleById(id: Long)
    fun updateArticleById(id: Long, title: String, content: String): Article
    fun updateArticleStatusById(id: Long, aspect: String, op: Int): Article
}