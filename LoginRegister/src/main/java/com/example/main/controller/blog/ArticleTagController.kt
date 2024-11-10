package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.ArticleTag
import com.example.main.dto.blog.ArticleTagDTO
import com.example.main.service.blog.ArticleTagService
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