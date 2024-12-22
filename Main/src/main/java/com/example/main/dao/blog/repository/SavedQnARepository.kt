package com.example.main.dao.blog.repository

import com.example.main.dao.blog.SavedQnA
import com.example.main.dao.blog.SavedQnAId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SavedQnARepository : JpaRepository<SavedQnA, SavedQnAId> {
    // 根据 userId 查找所有点赞的问贴
    fun findById_UserId(userId: Long): List<SavedQnA>

    // 根据 userId 和 qnaId 删除点赞记录
    fun deleteById_UserIdAndId_QnaId(userId: Long, qnaId: Long)
}
