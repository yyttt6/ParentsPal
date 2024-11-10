package com.example.demo.controller

import com.example.demo.Response
import com.example.demo.dao.Comment
import com.example.demo.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CommentController {
    @Autowired
    private lateinit var commentService: CommentService

    @GetMapping("/comment/{id}")
    fun getComment(@PathVariable("id") id: Long): Response<Comment> {
        return Response.newSuccess(commentService.getCommentById(id))
    }

    @PostMapping("/comment")
    fun createComment(@RequestBody comment: Comment): Response<Long?> {
        return Response.newSuccess(commentService.createComment(comment))
    }

    @DeleteMapping("/comment/{id}")
    fun deleteComment(@PathVariable id: Long) {
        commentService.deleteCommentById(id)
    }

    @PutMapping("/comment/{id}")
    fun updateComment(@PathVariable id: Long, @RequestParam(required = false) content: String): Response<Comment> {
        return Response.newSuccess(commentService.updateCommentById(id, content))
    }

    @PutMapping("/comment/{aspect}/{id}&{op}")
    fun updateCommentStatus(
        @PathVariable id: Long,
        @PathVariable aspect: String,
        @PathVariable op: Int
    ): Response<Comment> {
        return Response.newSuccess(commentService.updateCommentStatusById(id, aspect, op))
    }
}