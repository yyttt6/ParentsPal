package com.example.main.service.blog.implement

import com.example.main.Response
import com.example.main.dao.blog.QnA
import com.example.main.dao.blog.repository.QnARepository
import com.example.main.dao.login.Parent
import com.example.main.dao.login.ParentRepository
import com.example.main.service.blog.QnAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class QnAServiceImpl : QnAService {
    @Autowired
    private lateinit var qnaRepository: QnARepository
    @Autowired
    private lateinit var userRepository: ParentRepository

    override fun getQnAById(id: Long): Response<QnA> {
        val qna = qnaRepository.findById(id)
        return if (qna.isPresent) {
            Response.newSuccess(qna.get())
        } else {
            Response.newFail("QnA not found with id: $id")
        }
    }

    override fun getQnAByUserId(userId: Long): List<QnA> {
        return qnaRepository.findByUserId(userId).ifEmpty { throw RuntimeException("No qna found for user with id: $userId") }
    }

    override fun createQnA(qna: QnA): Long? {
        // 寻找用户id
        val userOptional: Optional<Parent> = userRepository.findById(qna.userId)

        // 查看用户是否存在
        if (userOptional.isPresent) {
            val user = userOptional.get()

            // 存在则将username赋给qna
            qna.username = user.name
            val savedQnA: QnA = qnaRepository.save(qna)
            return savedQnA.qnaId

        } else {
            // 不存在则报错
            throw NoSuchElementException("No user found with id: ${qna.userId}")
        }
    }

    override fun deleteQnAById(id: Long) {
        qnaRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }
        qnaRepository.deleteById(id)
    }

    @Transactional
    override fun updateQnAById(id: Long, title: String, content: String): QnA {
        // 寻找帖子id
        val qnaOptional: Optional<QnA> = qnaRepository.findById(id)

        // 查看帖子是否存在
        if (qnaOptional.isPresent) {
            val qna = qnaOptional.get()

            qna.title = title
            qna.content = content
            qna.time = LocalDateTime.now()

            val savedQnA: QnA = qnaRepository.save(qna)
            return savedQnA

        } else {
            // 不存在则报错
            throw NoSuchElementException("No qna found with id: $id")
        }
    }

    @Transactional
    override fun updateQnAStatusById(id: Long, aspect: String, op: Int): QnA {
        // Find the qna by ID
        val qnaOptional: Optional<QnA> = qnaRepository.findById(id)

        // Check if the qna exists
        if (!qnaOptional.isPresent) {
            throw NoSuchElementException("No qna found with id: $id")
        }

        val qna = qnaOptional.get()

        // Handle likes and saves updates
        when (aspect) {
            "likes" -> {
                when (op) {
                    1 -> qna.likes++ // Increment likes
                    2 -> qna.likes-- // Decrement likes
                    else -> throw IllegalArgumentException("Invalid operation for likes: $op")
                }
            }
            "saves" -> {
                when (op) {
                    1 -> qna.saves++ // Increment saves
                    2 -> qna.saves-- // Decrement saves
                    else -> throw IllegalArgumentException("Invalid operation for saves: $op")
                }
            }
            else -> throw IllegalArgumentException("Invalid aspect: $aspect")
        }

        // Save and return the updated qna
        return qnaRepository.save(qna)
    }

}