package com.example.main.dao.blog

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "qna_comment")
class QnAComment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_comment_id")
    var qnaCommentId: Long? = null,

    @Column(name = "qna_id")
    var qnaId: Long = 0,

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
