package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.QnAComment
import com.example.main.dto.blog.QnACommentDTO

interface QnACommentService {
    fun getQnACommentById(qnaCommentId: Long): Response<QnAComment>
    fun getQnACommentsByCategoryAndId(category: String, qnaId: Long, userId: Long): Response<List<QnAComment>>
    fun createQnAComment(qnaCommentDTO: QnACommentDTO): Response<Long?>
    fun deleteQnACommentById(qnaCommentId: Long): Response<Void>
    fun updateQnACommentById(qnaCommentId: Long, qnaCommentDTO: QnACommentDTO): Response<QnAComment>
    fun updateQnACommentStatusById(userId: Long, qnaCommentId: Long, aspect: String, op: String): Response<QnAComment>

}