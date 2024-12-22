package com.example.main.service.blog

import com.example.main.Response
import com.example.main.dao.blog.QnA
import com.example.main.dto.blog.QnADTO

interface QnAService {
    fun getQnAById(id: Long): Response<QnA>
    fun getQnAsByCategoryAndUserId(category: String, userId: Long): Response<List<QnA>>
    fun searchQnAByKeyword(keyword: String, page: Int, pageSize: Int): Response<List<QnA>>
    fun getHotQnA(): Response<List<QnA>>
    fun createQnA(qnaDTO: QnADTO): Response<Long?>
    fun updateQnAById(qnaDTO: QnADTO, qnaId: Long): Response<QnA>
    fun deleteQnAById(id: Long): Response<Void>
    fun updateQnAStatusById(userId: Long, qnaId: Long, aspect: String, op: String): Response<QnA>
}