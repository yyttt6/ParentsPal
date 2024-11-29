package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.QnA

interface QnAService {
    fun getQnAById(id: Long): Response<QnA>
    fun getQnAByUserId(userId: Long): List<QnA>
    fun createQnA(qna: QnA): Long?
    fun deleteQnAById(id: Long)
    fun updateQnAById(id: Long, title: String, content: String): QnA
    fun updateQnAStatusById(id: Long, aspect: String, op: Int): QnA
}