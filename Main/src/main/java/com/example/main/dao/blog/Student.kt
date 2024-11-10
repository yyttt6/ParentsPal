package com.example.main.dao.blog

import jakarta.persistence.*

@Entity
@Table(name = "student")
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "age")
    var age: Int
) {
    // No-arg constructor for JPA
    constructor() : this(null, "", "", 0)
}

