package com.example.main.dao.blog.repository

import com.example.main.dao.blog.LikedQnAComment
import com.example.main.dao.blog.LikedQnACommentId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikedQnACommentRepository : JpaRepository<LikedQnAComment, LikedQnACommentId> {
    // 根据 userId 查找所有点赞的问贴
    fun findById_UserId(userId: Long): List<LikedQnAComment>

    // 根据 userId 和 qnaCommentId 删除点赞记录
    fun deleteById_UserIdAndId_QnaCommentId(userId: Long, commentId: Long)
}
