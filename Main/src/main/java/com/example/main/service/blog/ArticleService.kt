package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.Article
import com.example.main.dto.blog.ArticleDTO

interface ArticleService {
    fun getArticleById(id: Long): Response<Article>
    fun getArticlesByCategoryAndUserId(category: String, userId: Long): Response<List<Article>>
    fun createArticle(articleDTO: ArticleDTO): Response<Long?>
    fun deleteArticleById(id: Long): Response<Void>
    fun updateArticleById(articleDTO: ArticleDTO, articleId: Long): Response<Article>
    fun updateArticleStatusById(userId: Long, articleId: Long, aspect: String, op: String): Response<Article>
    fun searchArticleByKeyword(keyword: String, page: Int, pageSize: Int): Response<List<Article>>
}