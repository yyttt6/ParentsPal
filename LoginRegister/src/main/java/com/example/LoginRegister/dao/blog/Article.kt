package com.example.demo.dao

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "article")
class Article (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    var articleId: Long? = null,
    @Column(name = "id_user")
    var idUser: Long,
    @Column(name = "username")
    var username: String,
    @Column(name = "title")
    var title: String,
    @Column(name = "content")
    var content: String,
    @Column(name = "likes")
    var likes: Int = 0,
    @Column(name = "saves")
    var saves: Int = 0,
    @Column(name = "time")
    var time: LocalDateTime = LocalDateTime.now(),
)