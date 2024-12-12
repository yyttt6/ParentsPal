package com.example.main.converter.blog

import com.example.main.dao.blog.Comment
import com.example.main.dto.blog.CommentDTO

// CommentConverter 用于将 CommentDTO 转换为 Comment 对象
class CommentConverter {
    companion object {
        // 将 CommentDTO 转换为 Comment
        fun convertComment(commentDTO: CommentDTO): Comment {
            return Comment(
                articleId = commentDTO.articleId!!,
                userId = commentDTO.userId!!,
                content = commentDTO.content!!
            )
        }
    }
}
