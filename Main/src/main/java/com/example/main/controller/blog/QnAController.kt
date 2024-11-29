package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.QnA
import com.example.main.service.blog.QnAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class QnAController {
    @Autowired
    private lateinit var qnaService: QnAService

    @GetMapping("/qna/{id}")
    fun getQnA(@PathVariable("id") id: Long): Response<QnA> {
        return qnaService.getQnAById(id)
    }

    @GetMapping("/user-qna/{userId}")
    fun getQnAList(@PathVariable("userId") userId: Long): Response<List<QnA>> {
        return Response.newSuccess(qnaService.getQnAByUserId(userId))
    }

    @PostMapping("/qna")
    fun createQnA(@RequestBody qna: QnA): Response<Long?> {
        return Response.newSuccess(qnaService.createQnA(qna))
    }

    @DeleteMapping("/qna/{id}")
    fun deleteQnA(@PathVariable id: Long) {
        qnaService.deleteQnAById(id)
    }

    @PutMapping("/qna/{id}")
    fun updateQnA(
        @PathVariable id: Long,
        @RequestParam(required = false) title: String,
        @RequestParam(required = false) content: String
    ): Response<QnA> {
        return Response.newSuccess(qnaService.updateQnAById(id, title, content))
    }

    @PutMapping("/qna/{aspect}/{id}&{op}")
    fun updateQnAStatus(
        @PathVariable id: Long,
        @PathVariable aspect: String,
        @PathVariable op: Int
    ): Response<QnA> {
        return Response.newSuccess(qnaService.updateQnAStatusById(id, aspect, op))
    }
}
