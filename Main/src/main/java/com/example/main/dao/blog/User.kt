package com.example.main.dao.blog

import jakarta.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var userId: Long? = null,

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "phonenumber", nullable = false)
    var phoneNumber: String
) {
    // No-arg constructor for JPA
    constructor() : this(null, "", "", "")
}
