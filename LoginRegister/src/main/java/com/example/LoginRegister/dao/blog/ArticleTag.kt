package com.example.demo.dao

import jakarta.persistence.*

@Entity
@Table(name = "article_tag")
class ArticleTag (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,
    @Column(name = "name")
    var name: String
)