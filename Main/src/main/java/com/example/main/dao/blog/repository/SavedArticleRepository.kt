package com.example.main.dao.blog.repository

import com.example.main.dao.blog.SavedArticle
import com.example.main.dao.blog.SavedArticleId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedArticleRepository : JpaRepository<SavedArticle, SavedArticleId> {
    // 根据 userId 查找所有点赞的文章
    fun findById_UserId(userId: Long): List<SavedArticle>

    // 根据 userId 和 articleId 删除点赞记录
    fun deleteById_UserIdAndId_ArticleId(userId: Long, articleId: Long)
}
