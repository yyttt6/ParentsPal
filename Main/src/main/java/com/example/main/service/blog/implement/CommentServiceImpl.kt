package com.example.main.service.blog.implement

import com.example.main.Response
import com.example.main.converter.blog.CommentConverter
import com.example.main.dao.blog.Article
import com.example.main.dao.blog.repository.ArticleRepository
import com.example.main.dao.blog.repository.CommentRepository
import com.example.main.dao.blog.Comment
import com.example.main.dao.blog.LikedComment
import com.example.main.dao.blog.LikedCommentId
import com.example.main.dao.blog.repository.LikedCommentRepository
import com.example.main.dao.login.Parent
import com.example.main.dao.login.ParentRepository
import com.example.main.dto.blog.CommentDTO
import com.example.main.service.blog.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
open class CommentServiceImpl: CommentService {
    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var likedCommentRepository: LikedCommentRepository
    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var userRepository: ParentRepository

    // 根据ID获取评论
    override fun getCommentById(commentId: Long): Response<Comment> {
        return try {
            val comment = commentRepository.findById(commentId)
            if (comment.isPresent) {
                Response.newSuccess(comment.get())
            } else {
                Response.newFail("未找到ID为: $commentId 的评论")
            }
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("获取评论时发生错误: ${e.message}")
        }
    }

    // 根据文章ID获取所有评论
    override fun getCommentsByCategoryAndId(category: String, articleId: Long, userId: Long): Response<List<Comment>> {
        return try {
            // 根据category判断查询逻辑
            if (category == "article") {
                // 如果category为'article'，根据articleId获取所有评论
                val comments = commentRepository.findByArticleId(articleId)
                if (comments.isNotEmpty()) {
                    Response.newSuccess(comments)
                } else {
                    Response.newFail("未找到ID为 $articleId 的文章的评论")
                }
            } else if (category == "liked") {
                // 如果category为'liked'，根据userId获取所有点赞的评论
                val likedComments = likedCommentRepository.findById_UserId(userId)
                if (likedComments.isNotEmpty()) {
                    // 获取所有与用户相关的commentId
                    val commentIds = likedComments.map { it.id.commentId }
                    val comments = commentRepository.findAllById(commentIds)
                    if (comments.isNotEmpty()) {
                        Response.newSuccess(comments)
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
    override fun createComment(commentDTO: CommentDTO): Response<Long?> {
        val comment: Comment = CommentConverter.convertComment(commentDTO)

        return try {
            // 检查用户是否存在
            val userOptional: Optional<Parent> = userRepository.findById(comment.userId)
            if (!userOptional.isPresent) {
                return Response.newFail("未找到ID为 ${comment.userId} 的用户")
            }

            // 获取用户信息
            val user = userOptional.get()
            comment.username = user.name

            // 检查文章是否存在
            val articleOptional: Optional<Article> = articleRepository.findById(comment.articleId)
            if (!articleOptional.isPresent) {
                return Response.newFail("未找到ID为 ${comment.articleId} 的文章")
            }

            // 保存评论
            val savedComment: Comment = commentRepository.save(comment)
            Response.newSuccess(savedComment.commentId)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("创建评论时发生错误: ${e.message}")
        }
    }


    // 根据ID删除评论
    override fun deleteCommentById(commentId: Long): Response<Void> {
        return try {
            commentRepository.findById(commentId).orElseThrow {
                IllegalArgumentException("ID: $commentId 不存在！")
            }
            commentRepository.deleteById(commentId)
            Response.newSuccess(null)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("删除评论时发生错误: ${e.message}")
        }
    }

    // 更新评论内容
    @Transactional
    override fun updateCommentById(commentId: Long, commentDTO: CommentDTO): Response<Comment> {
        return try {
            val commentOptional: Optional<Comment> = commentRepository.findById(commentId)

            if (commentOptional.isPresent) {
                val comment = commentOptional.get()
                comment.content = commentDTO.content!!
                comment.time = LocalDateTime.now()

                val savedComment: Comment = commentRepository.save(comment)
                Response.newSuccess(savedComment)
            } else {
                Response.newFail("未找到ID为: $commentId 的评论")
            }
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("更新评论时发生错误: ${e.message}")
        }
    }

    // 更新评论状态（例如点赞）
    @Transactional
    override fun updateCommentStatusById(userId: Long, commentId: Long, aspect: String, op: String): Response<Comment> {
        return try {
            val commentOptional: Optional<Comment> = commentRepository.findById(commentId)

            if (!commentOptional.isPresent) {
                return Response.newFail("未找到ID为: $commentId 的评论")
            }

            val comment = commentOptional.get()

            // 处理点赞等操作
            when (aspect) {
                "likes" -> {
                    val likedCommentId = LikedCommentId(userId, commentId)
                    val existingLike = likedCommentRepository.findById(likedCommentId)

                    when (op) {
                        "incr" -> {
                            // 如果用户未点赞过该评论，则增加点赞数，并添加记录到 LikedComment
                            if (!existingLike.isPresent) {  // 如果不存在点赞记录
                                comment.likes++  // 增加点赞数
                                likedCommentRepository.save(LikedComment(likedCommentId))  // 保存点赞记录
                            } else {
                                return Response.newFail("您已经点赞过该评论，不能重复点赞")
                            }
                        }
                        "decr" -> {
                            // 如果用户已经点赞过该评论，则减少点赞数，并删除记录
                            if (existingLike.isPresent) {  // 如果存在点赞记录
                                comment.likes--  // 减少点赞数
                                likedCommentRepository.deleteById_UserIdAndId_CommentId(userId, commentId)  // 删除点赞记录
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
            val updatedComment = commentRepository.save(comment)
            Response.newSuccess(updatedComment)
        } catch (e: Exception) {
            // 捕获异常并返回错误响应
            Response.newFail("更新评论状态时发生错误: ${e.message}")
        }
    }

}
