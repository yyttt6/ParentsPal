package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.Comment
import com.example.main.dto.blog.CommentDTO

interface CommentService {
    fun getCommentById(commentId: Long): Response<Comment>
    fun getCommentsByCategoryAndId(category: String, articleId: Long, userId: Long): Response<List<Comment>>
    fun createComment(commentDTO: CommentDTO): Response<Long?>
    fun deleteCommentById(commentId: Long): Response<Void>
    fun updateCommentById(commentId: Long, commentDTO: CommentDTO): Response<Comment>
    fun updateCommentStatusById(userId: Long, commentId: Long, aspect: String, op: String): Response<Comment>

}