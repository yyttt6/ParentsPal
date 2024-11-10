package com.example.main.service.implement

import com.example.main.dao.blog.User
import com.example.main.dao.blog.repository.CommentRepository
import com.example.main.dao.blog.Comment
import com.example.main.dao.login.ParentRepository
import com.example.main.service.blog.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class CommentServiceImpl: CommentService {
    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var userRepository: ParentRepository

    override fun getCommentById(id: Long): Comment {
        return commentRepository.findById(id).orElseThrow{ RuntimeException("Comment not found") }
    }

    override fun createComment(comment: Comment): Long? {
        // 寻找评论id
        val userOptional: Optional<User> = userRepository.findById(comment.userId)

        // 查看用户是否存在
        if (userOptional.isPresent) {
            val user = userOptional.get()

            // 存在则将username赋给comment
            comment.username = user.username
            val savedComment: Comment = commentRepository.save(comment)
            return savedComment.commentId

        } else {
            // 不存在则报错
            throw NoSuchElementException("No user found with id: ${comment.userId}")
        }
    }

    override fun deleteCommentById(id: Long) {
        commentRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }
        commentRepository.deleteById(id)
    }

    @Transactional
    override fun updateCommentById(id: Long, content: String): Comment {
        // 寻找评论id
        val commentOptional: Optional<Comment> = commentRepository.findById(id)

        // 查看评论是否存在
        if (commentOptional.isPresent) {
            val comment = commentOptional.get()

            comment.content = content
            comment.time = LocalDateTime.now()

            val savedComment: Comment = commentRepository.save(comment)
            return savedComment
        } else {
            // 不存在则报错
            throw NoSuchElementException("No comment found with id: $id")
        }
    }

    @Transactional
    override fun updateCommentStatusById(id: Long, aspect: String, op: Int): Comment {
        // Find the comment by ID
        val commentOptional: Optional<Comment> = commentRepository.findById(id)

        // Check if the comment exists
        if (!commentOptional.isPresent) {
            throw NoSuchElementException("No comment found with id: $id")
        }

        val comment = commentOptional.get()

        // Handle likes and saves updates
        when (aspect) {
            "likes" -> {
                when (op) {
                    1 -> comment.likes++ // Increment likes
                    2 -> comment.likes-- // Decrement likes
                    else -> throw IllegalArgumentException("Invalid operation for likes: $op")
                }
            }
            else -> throw IllegalArgumentException("Invalid aspect: $aspect")
        }

        // Save and return the updated comment
        return commentRepository.save(comment)
    }
    
}