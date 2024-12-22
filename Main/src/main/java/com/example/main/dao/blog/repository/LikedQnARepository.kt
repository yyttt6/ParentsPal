package com.example.main.dao.blog.repository

import com.example.main.dao.blog.LikedQnA
import com.example.main.dao.blog.LikedQnAId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikedQnARepository : JpaRepository<LikedQnA, LikedQnAId> {
    // 根据 userId 查找所有点赞的问贴
    fun findById_UserId(userId: Long): List<LikedQnA>

    // 根据 userId 和 qnaId 删除点赞记录
    fun deleteById_UserIdAndId_QnaId(userId: Long, qnaId: Long)
}
