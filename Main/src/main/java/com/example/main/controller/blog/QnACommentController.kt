package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.QnAComment
import com.example.main.dto.blog.QnACommentDTO
import com.example.main.service.blog.QnACommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class QnACommentController {
    @Autowired
    private lateinit var qnaCommentService: QnACommentService

    @GetMapping("/api/qnaComment/{qnaCommentId}")
    fun getQnAComment(@PathVariable("qnaCommentId") qnaCommentId: Long): Response<QnAComment> {
        return qnaCommentService.getQnACommentById(qnaCommentId)
    }

    @GetMapping("/api/{category}-qnaComment")
    fun getQnACommentList(
        @PathVariable("category") category: String,
        @RequestParam("qnaId", defaultValue = "0") qnaId: Long,
        @RequestParam("userId", defaultValue = "0") userId: Long
    ): Response<List<QnAComment>> {
        return qnaCommentService.getQnACommentsByCategoryAndId(category, qnaId, userId)
    }


    @PostMapping("/api/qnaComment")
    fun createQnAComment(@RequestBody qnaCommentDTO: QnACommentDTO): Response<Long?> {
        return qnaCommentService.createQnAComment(qnaCommentDTO)
    }

    @PostMapping("/api/qnaComment/{qnaCommentId}")
    fun updateQnAComment(@PathVariable qnaCommentId: Long, @RequestBody qnaComment: QnACommentDTO): Response<QnAComment> {
        return qnaCommentService.updateQnACommentById(qnaCommentId, qnaComment)
    }

    @DeleteMapping("/api/qnaComment/{qnaCommentId}")
    fun deleteQnAComment(@PathVariable qnaCommentId: Long) {
        qnaCommentService.deleteQnACommentById(qnaCommentId)
    }

    @PutMapping("/api/qnaComment/{userId}/{aspect}/{qnaCommentId}&{op}")
    fun updateQnACommentStatus(
        @PathVariable userId: Long,
        @PathVariable aspect: String,
        @PathVariable qnaCommentId: Long,
        @PathVariable op: String
    ): Response<QnAComment> {
        return qnaCommentService.updateQnACommentStatusById(userId, qnaCommentId, aspect, op)
    }
}