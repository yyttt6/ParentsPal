package com.example.demo.dao

import jakarta.persistence.*

@Entity
@Table(name = "article_tag_list")
class ArticleTagList (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article_tag_list")
    var articleTagListId: Long? = null,
    @Column(name = "id_article")
    var articleId: Long? = null,
    @Column(name = "id_article_tag")
    var articleTagId: Long? = null
)