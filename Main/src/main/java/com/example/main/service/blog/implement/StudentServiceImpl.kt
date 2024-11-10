package com.example.demo.service.implement

import com.example.demo.converter.StudentConverter
import com.example.demo.dao.Student
import com.example.demo.dao.repository.StudentRepository
import com.example.demo.dto.StudentDTO
import com.example.demo.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import org.springframework.util.StringUtils

@Service
class StudentServiceImpl: StudentService {

    @Autowired
    private lateinit var studentRepository: StudentRepository

    override fun getStudentById(id: Long): StudentDTO {
        return StudentConverter.convertStudent(studentRepository.findById(id).orElseThrow { RuntimeException("Student not found with id: $id") })
    }

    override fun createStudent(studentDTO: StudentDTO): Long? {
        val studentList: List<Student> = studentRepository.findByEmail(studentDTO.email)
        if (!CollectionUtils.isEmpty(studentList)) {
            throw IllegalArgumentException("email: $studentDTO.email has been taken")
        }
        val student: Student = studentRepository.save(StudentConverter.convertStudent(studentDTO))
        return student.id
    }

    override fun deleteStudentById(id: Long) {
        studentRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }
        studentRepository.deleteById(id)
    }

    @Transactional
    override fun updateStudentById(id: Long, name: String, email: String): StudentDTO {
        val studentInDb: Student = studentRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }

        if (StringUtils.hasLength(name) && studentInDb.name != name) {
            studentInDb.name = name
        }

        if (StringUtils.hasLength(email) && studentInDb.email != email) {
            studentInDb.email = email
        }

        val student: Student = studentRepository.save(studentInDb)
        return StudentConverter.convertStudent(student)
    }
}
