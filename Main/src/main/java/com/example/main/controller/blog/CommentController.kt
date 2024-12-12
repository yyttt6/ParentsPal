package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.Comment
import com.example.main.dto.blog.CommentDTO
import com.example.main.service.blog.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CommentController {
    @Autowired
    private lateinit var commentService: CommentService

    @GetMapping("/api/comment/{commentId}")
    fun getComment(@PathVariable("commentId") commentId: Long): Response<Comment> {
        return commentService.getCommentById(commentId)
    }

    @GetMapping("/api/{category}-comment")
    fun getCommentList(
        @PathVariable("category") category: String,
        @RequestParam("articleId", defaultValue = "0") articleId: Long,
        @RequestParam("userId", defaultValue = "0") userId: Long
    ): Response<List<Comment>> {
        return commentService.getCommentsByCategoryAndId(category, articleId, userId)
    }


    @PostMapping("/api/comment")
    fun createComment(@RequestBody commentDTO: CommentDTO): Response<Long?> {
        return commentService.createComment(commentDTO)
    }

    @PostMapping("/api/comment/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody comment: CommentDTO): Response<Comment> {
        return commentService.updateCommentById(commentId, comment)
    }

    @DeleteMapping("/api/comment/{commentId}")
    fun deleteComment(@PathVariable commentId: Long) {
        commentService.deleteCommentById(commentId)
    }

    @PutMapping("/api/comment/{userId}/{aspect}/{commentId}&{op}")
    fun updateCommentStatus(
        @PathVariable userId: Long,
        @PathVariable aspect: String,
        @PathVariable commentId: Long,
        @PathVariable op: String
    ): Response<Comment> {
        return commentService.updateCommentStatusById(userId, commentId, aspect, op)
    }
}