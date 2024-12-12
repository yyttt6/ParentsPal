package com.example.main.dao.blog

import jakarta.persistence.*
import java.io.Serializable

// 复合主键类（嵌入式主键）
@Embeddable
data class SavedArticleId(
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0, // 用户ID

    @Column(name = "article_id", nullable = false)
    val articleId: Long = 0  // 文章ID，默认为非空
) : Serializable

// 实体类定义
@Entity
@Table(name = "saved_article")
class SavedArticle(
    @EmbeddedId
    val id: SavedArticleId = SavedArticleId() // 默认初始化复合主键
) {
    // 无参构造函数，默认初始化 id
    constructor() : this(SavedArticleId())  // JPA 需要一个无参构造函数
}
