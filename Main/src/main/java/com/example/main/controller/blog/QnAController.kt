package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.QnA
import com.example.main.dto.blog.QnADTO
import com.example.main.service.blog.QnAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class QnAController {
    @Autowired
    private lateinit var qnaService: QnAService

    // 根据问贴ID获取问贴
    @GetMapping("/api/qna/{qnaId}")
    fun getQnA(@PathVariable("qnaId") qnaId: Long): Response<QnA> {
        return qnaService.getQnAById(qnaId)
    }

    // 根据用户ID获取对应种类的所有问贴列表
    @GetMapping("/api/{category}-qna/{userId}")
    fun getQnAList(@PathVariable("category") category: String, @PathVariable("userId") userId: Long): Response<List<QnA>> {
        return qnaService.getQnAsByCategoryAndUserId(category, userId)
    }

    // 根据关键词搜索问贴，支持分页
    @GetMapping("/api/qna/search")
    fun searchForQnA(
        @RequestParam("queryKeyword") queryKeyword: String, // 搜索关键词，用于查找相关的问贴
        @RequestParam("page", defaultValue = "1") page: Int, // 页码，默认为第 1 页
        @RequestParam("pageSize", defaultValue = "10") pageSize: Int // 每页返回的记录数，默认为 10 条
    ): Response<List<QnA>> {
        return qnaService.searchQnAByKeyword(queryKeyword, page, pageSize)
    }

    @GetMapping("/api/qna/hot")
    fun getHotQnA(): Response<List<QnA>> {
        return qnaService.getHotQnA()
    }

    // 创建一篇新问贴
    @PostMapping("/api/qna")
    fun createQnA(@RequestBody qnaDTO: QnADTO): Response<Long?> {
        return qnaService.createQnA(qnaDTO)
    }

    // 更新指定问贴ID的问贴信息
    @PostMapping("/api/qna/update/{qnaId}")
    fun updateQnA(@RequestBody qnaDTO: QnADTO, @PathVariable("qnaId") qnaId: Long): Response<QnA> {
        return qnaService.updateQnAById(qnaDTO, qnaId)
    }

    // 根据问贴ID删除问贴
    @DeleteMapping("/api/qna/{qnaId}")
    fun deleteQnA(@PathVariable qnaId: Long) {
        qnaService.deleteQnAById(qnaId)
    }

    // 更新问贴状态，例如：发布、草稿等
    @PutMapping("/api/qna/{userId}/{aspect}/{qnaId}&{op}")
    fun updateQnAStatus(
        @PathVariable userId: Long,
        @PathVariable aspect: String, // 问贴的状态方面（如：发布、草稿等）
        @PathVariable qnaId: Long,
        @PathVariable op: String // 操作类型，通常用于表示操作（如：发布/取消发布）
    ): Response<QnA> {
        return qnaService.updateQnAStatusById(userId, qnaId, aspect, op)
    }
}
