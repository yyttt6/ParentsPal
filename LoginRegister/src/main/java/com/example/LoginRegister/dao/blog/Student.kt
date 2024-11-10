package com.example.demo.dao

import jakarta.persistence.*

@Entity
@Table(name = "student")
data class Student(
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
)
