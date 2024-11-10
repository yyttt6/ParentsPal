package com.example.demo.controller

import com.example.demo.Response
import com.example.demo.dto.StudentDTO
import com.example.demo.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class StudentController {
    @Autowired
    private lateinit var studentService: StudentService

    @GetMapping("/student/{id}")
    fun getStudent(@PathVariable id: Long): Response<StudentDTO> {
        return Response.newSuccess(studentService.getStudentById(id))
    }

    @PostMapping("/student")
    fun createStudent(@RequestBody studentDTO: StudentDTO): Response<Long?> {
        return Response.newSuccess(studentService.createStudent(studentDTO))
    }

    @DeleteMapping("/student/{id}")
    fun deleteStudent(@PathVariable id: Long) {
        studentService.deleteStudentById(id)
    }

    @PutMapping("/student/{id}")
    fun updateStudent(@PathVariable id: Long, @RequestParam(required = false) name: String, @RequestParam(required = false) email: String): Response<StudentDTO> {
        return Response.newSuccess(studentService.updateStudentById(id, name, email))
    }
}