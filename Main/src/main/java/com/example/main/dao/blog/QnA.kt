package com.example.main.dao.blog

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "qna")
class QnA (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    var qnaId: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "username", nullable = false)
    var username: String = "",

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "content", nullable = false)
    var content: String = "",

    @Column(name = "likes")
    var likes: Int = 0,

    @Column(name = "saves")
    var saves: Int = 0,

    @Column(name = "comments")
    var comments: Int = 0,

    @Column(name = "time", nullable = false)
    var time: LocalDateTime = LocalDateTime.now(),
) {
    // No-arg constructor required by JPA
    constructor() : this(null, 0, "", "", "", 0, 0, 0, LocalDateTime.now())
}
