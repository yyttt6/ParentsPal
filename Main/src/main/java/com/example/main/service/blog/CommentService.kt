package com.example.demo.service

import com.example.demo.dao.Comment

interface CommentService {
    fun getCommentById(id: Long): Comment
    fun createComment(comment: Comment): Long?
    fun deleteCommentById(id: Long)
    fun updateCommentById(id: Long, content: String): Comment
    fun updateCommentStatusById(id: Long, aspect: String, op: Int): Comment

}