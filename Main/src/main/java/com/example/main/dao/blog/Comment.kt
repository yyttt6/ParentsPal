package com.example.main.dao.blog

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    var commentId: Long? = null,

    @Column(name = "article_id")
    var articleId: Long = 0,

    @Column(name = "user_id")
    var userId: Long = 0,

    @Column(name = "username")
    var username: String = "",

    @Column(name = "content")
    var content: String,

    @Column(name = "likes")
    var likes: Int = 0,

    @Column(name = "time_created")
    var time: LocalDateTime = LocalDateTime.now(),
) {
    // JPA 要求的无参构造函数
    constructor() : this(null, 0, 0, "", "", 0, LocalDateTime.now())
}
