package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.QnAComment
import com.example.main.dto.blog.QnACommentDTO

interface QnACommentService {
    fun getQnACommentById(commentId: Long): Response<QnAComment>
    fun getQnACommentsByCategoryAndId(category: String, articleId: Long, userId: Long): Response<List<QnAComment>>
    fun createQnAComment(commentDTO: QnACommentDTO): Response<Long?>
    fun deleteQnACommentById(commentId: Long): Response<Void>
    fun updateQnACommentById(commentId: Long, commentDTO: QnACommentDTO): Response<QnAComment>
    fun updateQnACommentStatusById(userId: Long, commentId: Long, aspect: String, op: String): Response<QnAComment>

}