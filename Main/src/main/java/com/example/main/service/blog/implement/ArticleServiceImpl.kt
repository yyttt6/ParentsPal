package com.example.main.service.blog.implement

import com.example.main.Response
import com.example.main.dao.blog.Article
import com.example.main.dao.blog.repository.ArticleRepository
import com.example.main.dao.login.Parent
import com.example.main.dao.login.ParentRepository
import com.example.main.service.blog.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class ArticleServiceImpl : ArticleService {
    @Autowired
    private lateinit var articleRepository: ArticleRepository
    @Autowired
    private lateinit var userRepository: ParentRepository

    override fun getArticleById(id: Long): Response<Article> {
        val article = articleRepository.findById(id)
        return if (article.isPresent) {
            Response.newSuccess(article.get())
        } else {
            Response.newFail("Article not found with id: $id")
        }
    }

    override fun getArticleByUserId(userId: Long): List<Article> {
        return articleRepository.findByUserId(userId).ifEmpty { throw RuntimeException("No articles found for user with id: $userId") }
    }

    override fun createArticle(article: Article): Long? {
        // 寻找用户id
        val userOptional: Optional<Parent> = userRepository.findById(article.userId)

        // 查看用户是否存在
        if (userOptional.isPresent) {
            val user = userOptional.get()

            // 存在则将username赋给article
            article.username = user.name
            val savedArticle: Article = articleRepository.save(article)
            return savedArticle.articleId

        } else {
            // 不存在则报错
            throw NoSuchElementException("No user found with id: ${article.userId}")
        }
    }

    override fun deleteArticleById(id: Long) {
        articleRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }
        articleRepository.deleteById(id)
    }

    @Transactional
    override fun updateArticleById(id: Long, title: String, content: String): Article {
        // 寻找帖子id
        val articleOptional: Optional<Article> = articleRepository.findById(id)

        // 查看帖子是否存在
        if (articleOptional.isPresent) {
            val article = articleOptional.get()

            article.title = title
            article.content = content
            article.time = LocalDateTime.now()

            val savedArticle: Article = articleRepository.save(article)
            return savedArticle

        } else {
            // 不存在则报错
            throw NoSuchElementException("No article found with id: $id")
        }
    }

    @Transactional
    override fun updateArticleStatusById(id: Long, aspect: String, op: Int): Article {
        // Find the article by ID
        val articleOptional: Optional<Article> = articleRepository.findById(id)

        // Check if the article exists
        if (!articleOptional.isPresent) {
            throw NoSuchElementException("No article found with id: $id")
        }

        val article = articleOptional.get()

        // Handle likes and saves updates
        when (aspect) {
            "likes" -> {
                when (op) {
                    1 -> article.likes++ // Increment likes
                    2 -> article.likes-- // Decrement likes
                    else -> throw IllegalArgumentException("Invalid operation for likes: $op")
                }
            }
            "saves" -> {
                when (op) {
                    1 -> article.saves++ // Increment saves
                    2 -> article.saves-- // Decrement saves
                    else -> throw IllegalArgumentException("Invalid operation for saves: $op")
                }
            }
            else -> throw IllegalArgumentException("Invalid aspect: $aspect")
        }

        // Save and return the updated article
        return articleRepository.save(article)
    }

}