package com.example.demo.controller

import com.example.demo.Response
import com.example.demo.dao.Article
import com.example.demo.service.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ArticleController {
    @Autowired
    private lateinit var articleService: ArticleService

    @GetMapping("/article/{id}")
    fun getArticle(@PathVariable("id") id: Long): Response<Article> {
        return articleService.getArticleById(id)
    }

    @GetMapping("/user-article/{userId}")
    fun getArticleList(@PathVariable("userId") userId: Long): Response<List<Article>> {
        return Response.newSuccess(articleService.getArticleByUserId(userId))
    }

    @PostMapping("/article")
    fun createArticle(@RequestBody article: Article): Response<Long?> {
        return Response.newSuccess(articleService.createArticle(article))
    }

    @DeleteMapping("/article/{id}")
    fun deleteArticle(@PathVariable id: Long) {
        articleService.deleteArticleById(id)
    }

    @PutMapping("/article/{id}")
    fun updateArticle(
        @PathVariable id: Long,
        @RequestParam(required = false) title: String,
        @RequestParam(required = false) content: String
    ): Response<Article> {
        return Response.newSuccess(articleService.updateArticleById(id, title, content))
    }

    @PutMapping("/article/{aspect}/{id}&{op}")
    fun updateArticleStatus(
        @PathVariable id: Long,
        @PathVariable aspect: String,
        @PathVariable op: Int
    ): Response<Article> {
        return Response.newSuccess(articleService.updateArticleStatusById(id, aspect, op))
    }
}
