package com.example.main.controller.blog

import com.example.main.Response
import com.example.main.dao.blog.Article
import com.example.main.dto.blog.ArticleDTO
import com.example.main.service.blog.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ArticleController {
    @Autowired
    private lateinit var articleService: ArticleService

    // 根据文章ID获取文章
    @GetMapping("/api/article/{articleId}")
    fun getArticle(@PathVariable("articleId") articleId: Long): Response<Article> {
        return articleService.getArticleById(articleId)
    }

    // 根据用户ID获取对应种类的所有文章列表
    @GetMapping("/api/{category}-article/{userId}")
    fun getArticleList(@PathVariable("category") category: String, @PathVariable("userId") userId: Long): Response<List<Article>> {
        return articleService.getArticlesByCategoryAndUserId(category, userId)
    }

    // 根据关键词搜索文章，支持分页
    @GetMapping("/api/article/search")
    fun searchForArticle(
        @RequestParam("queryKeyword") queryKeyword: String, // 搜索关键词，用于查找相关的文章
        @RequestParam("page", defaultValue = "1") page: Int, // 页码，默认为第 1 页
        @RequestParam("pageSize", defaultValue = "10") pageSize: Int // 每页返回的记录数，默认为 10 条
    ): Response<List<Article>> {
        return articleService.searchArticleByKeyword(queryKeyword, page, pageSize)
    }

    // 创建一篇新文章
    @PostMapping("/api/article")
    fun createArticle(@RequestBody articleDTO: ArticleDTO): Response<Long?> {
        return articleService.createArticle(articleDTO)
    }

    // 更新指定文章ID的文章信息
    @PostMapping("/api/article/update/{articleId}")
    fun updateArticle(@RequestBody articleDTO: ArticleDTO, @PathVariable("articleId") articleId: Long): Response<Article> {
        return articleService.updateArticleById(articleDTO, articleId)
    }

    // 根据文章ID删除文章
    @DeleteMapping("/api/article/{articleId}")
    fun deleteArticle(@PathVariable articleId: Long) {
        articleService.deleteArticleById(articleId)
    }

    // 更新文章状态，例如：发布、草稿等
    @PutMapping("/api/article/{userId}/{aspect}/{articleId}&{op}")
    fun updateArticleStatus(
        @PathVariable userId: Long,
        @PathVariable aspect: String, // 文章的状态方面（如：发布、草稿等）
        @PathVariable articleId: Long,
        @PathVariable op: String // 操作类型，通常用于表示操作（如：发布/取消发布）
    ): Response<Article> {
        return articleService.updateArticleStatusById(userId, articleId, aspect, op)
    }
}
