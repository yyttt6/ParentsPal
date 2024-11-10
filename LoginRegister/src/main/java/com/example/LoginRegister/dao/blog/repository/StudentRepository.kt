package com.example.demo.dao.repository

import com.example.demo.dao.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository: JpaRepository<Student, Long>{
    fun findByEmail(email: String): List<Student>
}