package com.example.main.dao.blog

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    var commentId: Long? = null,

    @Column(name = "id_article")
    var articleId: Long? = null,

    @Column(name = "id_user")
    var userId: Long,

    @Column(name = "username")
    var username: String = "",

    @Column(name = "content")
    var content: String,

    @Column(name = "likes")
    var likes: Int = 0,

    @Column(name = "time")
    var time: LocalDateTime = LocalDateTime.now(),
) {
    // No-arg constructor required by JPA
    constructor() : this(null, null, 0, "", "", 0, LocalDateTime.now())
}
