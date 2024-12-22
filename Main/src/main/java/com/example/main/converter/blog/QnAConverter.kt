package com.example.main.converter.blog

import com.example.main.dao.blog.QnA
import com.example.main.dto.blog.QnADTO

// QnAConverter 用于将 QnADTO 转换为 QnA 对象
class QnAConverter {
    companion object {
        // 将 QnADTO 转换为 QnA
        fun convertQnA(qnaDTO: QnADTO): QnA {
            return QnA(
                userId = qnaDTO.userId!!,
                title = qnaDTO.title!!,
                content = qnaDTO.content!!
            )
        }
    }
}
