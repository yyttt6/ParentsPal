package com.example.main.dao.blog.repository

import com.example.main.dao.blog.LikedComment
import com.example.main.dao.blog.LikedCommentId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikedCommentRepository : JpaRepository<LikedComment, LikedCommentId> {
    // 根据 userId 查找所有点赞的文章
    fun findById_UserId(userId: Long): List<LikedComment>

    // 根据 userId 和 commentId 删除点赞记录
    fun deleteById_UserIdAndId_CommentId(userId: Long, commentId: Long)
}
