package com.example.demo.service

import com.example.demo.dto.StudentDTO

interface StudentService {
    fun getStudentById(id: Long): StudentDTO
    fun createStudent(studentDTO: StudentDTO): Long?
    fun deleteStudentById(id: Long)
    fun updateStudentById(id: Long, name: String, email: String): StudentDTO
}