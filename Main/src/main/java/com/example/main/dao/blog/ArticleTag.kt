package com.example.main.dao.blog

import jakarta.persistence.*

@Entity
@Table(name = "article_tag")
class ArticleTag (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name")
    var name: String = "" // default value for no-arg constructor
) {
    // No-arg constructor required by JPA
    constructor() : this(null, "")
}
