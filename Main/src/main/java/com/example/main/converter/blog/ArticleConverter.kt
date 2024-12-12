package com.example.main.converter.blog

import com.example.main.dao.blog.Article
import com.example.main.dto.blog.ArticleDTO

// ArticleConverter 用于将 ArticleDTO 转换为 Article 对象
class ArticleConverter {
    companion object {
        // 将 ArticleDTO 转换为 Article
        fun convertArticle(articleDTO: ArticleDTO): Article {
            return Article(
                userId = articleDTO.userId!!,
                title = articleDTO.title!!,
                content = articleDTO.content!!
            )
        }
    }
}
