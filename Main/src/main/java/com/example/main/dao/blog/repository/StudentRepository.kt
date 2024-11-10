package com.example.main.dao.blog.repository

import com.example.main.dao.blog.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository: JpaRepository<Student, Long>{
    fun findByEmail(email: String): List<Student>
}