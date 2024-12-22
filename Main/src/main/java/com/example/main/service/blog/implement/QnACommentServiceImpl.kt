package com.example.main.service.blog.implement

import com.example.main.Response
import com.example.main.converter.blog.QnACommentConverter
import com.example.main.dao.blog.QnA
import com.example.main.dao.blog.repository.QnARepository
import com.example.main.dao.blog.QnAComment
import com.example.main.dao.blog.LikedQnAComment
import com.example.main.dao.blog.LikedQnACommentId
import com.example.main.dao.blog.repository.LikedQnACommentRepository
import com.example.main.dao.blog.repository.QnACommentRepository
import com.example.main.dao.login.Parent
import com.example.main.dao.login.ParentRepository
import com.example.main.dto.blog.QnACommentDTO
import com.example.main.service.blog.QnACommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class QnACommentServiceImpl: QnACommentService {
    @Autowired
    private lateinit var qnaRepository: QnARepository

    @Autowired
    private lateinit var likedQnACommentRepository: LikedQnACommentRepository
    @Autowired
    private lateinit var qnaCommentRepository: QnACommentRepository
    @Autowired
    private lateinit var userRepository: ParentRepository

    // 根据ID获取评论
    override fun getQnACommentById(qnaCommentId: Long): Response<QnAComment> {
        return try {
            val qnaComment = qnaCommentRepository.findById(qnaCommentId)
            if (qnaComment.isPresent) {
                Response.newSuccess(qnaComment.get())
            } else {
                Response.newFail("未找到ID为: $qnaCommentId 的评论")
            }
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("获取评论时发生错误: ${e.message}")
        }
    }

    // 根据问贴ID获取所有评论
    override fun getQnACommentsByCategoryAndId(category: String, qnaId: Long, userId: Long): Response<List<QnAComment>> {
        return try {
            // 根据category判断查询逻辑
            if (category == "qna") {
                // 如果category为'qna'，根据qnaId获取所有评论
                val qnaComments = qnaCommentRepository.findByQnaId(qnaId)
                if (qnaComments.isNotEmpty()) {
                    Response.newSuccess(qnaComments)
                } else {
                    Response.newFail("未找到ID为 $qnaId 的问贴的评论")
                }
            } else if (category == "liked") {
                // 如果category为'liked'，根据userId获取所有点赞的评论
                val likedQnAComments = likedQnACommentRepository.findById_UserId(userId)
                if (likedQnAComments.isNotEmpty()) {
                    // 获取所有与用户相关的qnaCommentId
                    val qnaCommentIds = likedQnAComments.map { it.id.qnaCommentId }
                    val qnaComments = qnaCommentRepository.findAllById(qnaCommentIds)
                    if (qnaComments.isNotEmpty()) {
                        Response.newSuccess(qnaComments)
                    } else {
                        Response.newFail("未找到ID为 $userId 的用户点赞的评论")
                    }
                } else {
                    Response.newFail("未找到ID为 $userId 的用户点赞的评论")
                }
            } else {
                Response.newFail("无效的category: $category")
            }
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("获取评论时发生错误: ${e.message}")
        }
    }


    // 创建评论
    override fun createQnAComment(qnaCommentDTO: QnACommentDTO): Response<Long?> {
        val qnaComment: QnAComment = QnACommentConverter.convertQnAComment(qnaCommentDTO)

        return try {
            // 检查用户是否存在
            val userOptional: Optional<Parent> = userRepository.findById(qnaComment.userId)
            if (!userOptional.isPresent) {
                return Response.newFail("未找到ID为 ${qnaComment.userId} 的用户")
            }

            // 获取用户信息
            val user = userOptional.get()
            qnaComment.username = user.name

            // 检查问贴是否存在
            val qnaOptional: Optional<QnA> = qnaRepository.findById(qnaComment.qnaId)
            if (!qnaOptional.isPresent) {
                return Response.newFail("未找到ID为 ${qnaComment.qnaId} 的问贴")
            }

            // 保存评论
            val savedQnAComment: QnAComment = qnaCommentRepository.save(qnaComment)
            Response.newSuccess(savedQnAComment.qnaCommentId)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("创建评论时发生错误: ${e.message}")
        }
    }


    // 根据ID删除评论
    override fun deleteQnACommentById(qnaCommentId: Long): Response<Void> {
        return try {
            qnaCommentRepository.findById(qnaCommentId).orElseThrow {
                IllegalArgumentException("ID: $qnaCommentId 不存在！")
            }
            qnaCommentRepository.deleteById(qnaCommentId)
            Response.newSuccess(null)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("删除评论时发生错误: ${e.message}")
        }
    }

    // 更新评论内容
    @Transactional
    override fun updateQnACommentById(qnaCommentId: Long, qnaCommentDTO: QnACommentDTO): Response<QnAComment> {
        return try {
            val qnaCommentOptional: Optional<QnAComment> = qnaCommentRepository.findById(qnaCommentId)

            if (qnaCommentOptional.isPresent) {
                val qnaComment = qnaCommentOptional.get()
                qnaComment.content = qnaCommentDTO.content!!
                qnaComment.time = LocalDateTime.now()

                val savedQnAComment: QnAComment = qnaCommentRepository.save(qnaComment)
                Response.newSuccess(savedQnAComment)
            } else {
                Response.newFail("未找到ID为: $qnaCommentId 的评论")
            }
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("更新评论时发生错误: ${e.message}")
        }
    }

    // 更新评论状态（例如点赞）
    @Transactional
    override fun updateQnACommentStatusById(userId: Long, qnaCommentId: Long, aspect: String, op: String): Response<QnAComment> {
        return try {
            val qnaCommentOptional: Optional<QnAComment> = qnaCommentRepository.findById(qnaCommentId)

            if (!qnaCommentOptional.isPresent) {
                return Response.newFail("未找到ID为: $qnaCommentId 的评论")
            }

            val qnaComment = qnaCommentOptional.get()

            // 处理点赞等操作
            when (aspect) {
                "likes" -> {
                    val likedQnACommentId = LikedQnACommentId(userId, qnaCommentId)
                    val existingLike = likedQnACommentRepository.findById(likedQnACommentId)

                    when (op) {
                        "incr" -> {
                            // 如果用户未点赞过该评论，则增加点赞数，并添加记录到 LikedQnAComment
                            if (!existingLike.isPresent) {  // 如果不存在点赞记录
                                qnaComment.likes++  // 增加点赞数
                                likedQnACommentRepository.save(LikedQnAComment(likedQnACommentId))  // 保存点赞记录
                            } else {
                                return Response.newFail("您已经点赞过该评论，不能重复点赞")
                            }
                        }
                        "decr" -> {
                            // 如果用户已经点赞过该评论，则减少点赞数，并删除记录
                            if (existingLike.isPresent) {  // 如果存在点赞记录
                                qnaComment.likes--  // 减少点赞数
                                likedQnACommentRepository.deleteById_UserIdAndId_QnaCommentId(userId, qnaCommentId)  // 删除点赞记录
                            } else {
                                return Response.newFail("未找到对应的点赞记录，无法取消点赞")
                            }
                        }
                        else -> return Response.newFail("点赞操作无效: $op")
                    }
                }
                else -> return Response.newFail("无效的状态字段: $aspect")
            }

            // 保存并返回更新后的评论
            val updatedQnAComment = qnaCommentRepository.save(qnaComment)
            Response.newSuccess(updatedQnAComment)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("更新评论状态时发生错误: ${e.message}")
        }
    }

}
