package com.example.demo.controller

import com.example.demo.Response
import com.example.demo.dao.ArticleTag
import com.example.demo.dto.ArticleTagDTO
import com.example.demo.service.ArticleTagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ArticleTagController {
    @Autowired
    private lateinit var articleTagService: ArticleTagService

    @GetMapping("/articleTag/{id}")
    fun getArticleTag(@PathVariable id: Long): Response<ArticleTag> {
        return Response.newSuccess(articleTagService.getArticleTagById(id))
    }

    @PostMapping("/articleTag")
    fun createArticleTag(@RequestBody articleTag: ArticleTagDTO): Response<Long?> {
        return Response.newSuccess(articleTagService.createArticleTag(articleTag))
    }

    @DeleteMapping("/articleTag/{id}")
    fun deleteArticleTag(@PathVariable id: Long) {
        articleTagService.deleteArticleTagById(id)
    }

    @PutMapping("/articleTag/{id}")
    fun updateArticleTag(@PathVariable id: Long, @RequestParam(required = false) name: String): Response<ArticleTag> {
        return Response.newSuccess(articleTagService.updateArticleTagById(id, name))
    }
}