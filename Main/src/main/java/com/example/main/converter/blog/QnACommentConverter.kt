package com.example.main.converter.blog

import com.example.main.dao.blog.QnAComment
import com.example.main.dto.blog.QnACommentDTO

// QnACommentConverter 用于将 QnACommentDTO 转换为 QnAComment 对象
class QnACommentConverter {
    companion object {
        // 将 QnACommentDTO 转换为 QnAComment
        fun convertQnAComment(qnaCommentDTO: QnACommentDTO): QnAComment {
            return QnAComment(
                qnaId = qnaCommentDTO.qnaId!!,
                userId = qnaCommentDTO.userId!!,
                content = qnaCommentDTO.content!!
            )
        }
    }
}
