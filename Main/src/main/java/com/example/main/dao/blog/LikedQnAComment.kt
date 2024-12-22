package com.example.main.dao.blog

import jakarta.persistence.*
import java.io.Serializable

// 复合主键类（嵌入式主键）
@Embeddable
data class LikedQnACommentId(
    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,  // 用户ID

    @Column(name = "qna_comment_id", nullable = false)
    val qnaCommentId: Long = 0  // 问贴ID，默认为非空
) : Serializable

// 实体类定义
@Entity
@Table(name = "liked_qna_comment")
class LikedQnAComment(
    @EmbeddedId
    val id: LikedQnACommentId = LikedQnACommentId() // 默认初始化复合主键
) {
    // 无参构造函数，默认初始化 id
    constructor() : this(LikedQnACommentId())  // JPA 需要一个无参构造函数
}
