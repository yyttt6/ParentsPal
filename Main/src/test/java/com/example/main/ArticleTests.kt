package com.example.main

import com.example.main.dto.blog.ArticleDTO
import com.example.main.service.blog.implement.ArticleServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
class ArticleServiceTests {

    @Autowired
    private lateinit var articleServiceImpl: ArticleServiceImpl // 使用实际实现

    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())  // 注册 JavaTimeModule，支持 LocalDateTime 序列化
    }

    private var testArticleId: Long = 3

    @Test
    fun testCreateArticle() {
        val article = ArticleDTO(userId = 1, title = "测试标题", content = "测试内容")

        // 调用 createArticle 方法
        val result = articleServiceImpl.createArticle(article)

        testArticleId = result.data!!

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果，确保文章创建成功且有文章ID
        assertTrue(result.isSuccess) // 确保创建成功
        assertNotNull(result.data) // 确保文章ID不为空
    }

    @Test
    fun testGetArticleByIdSuccess() {
        val articleId = testArticleId

        // 调用 getArticleById 方法
        val result = articleServiceImpl.getArticleById(articleId)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果，确保找到文章
        assertTrue(result.isSuccess)
        assertNotNull(result.data)
        assertEquals(articleId, result.data?.articleId) // 确保返回的文章ID正确
    }

    @Test
    fun testGetArticleByIdFail() {
        val articleId = 1L

        // 调用 getArticleById 方法
        val result = articleServiceImpl.getArticleById(articleId)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果，确保找到文章
        assertTrue(!result.isSuccess)
        assertNull(result.data)
    }

    @Test
    fun testGetUserArticles() {
        val userId = 1L // 假设用户ID为1
        val category = "user"

        // 调用 getArticlesByCategoryAndUserId 方法
        val result = articleServiceImpl.getArticlesByCategoryAndUserId(category, userId)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果，确保返回了文章列表
        assertTrue(result.isSuccess)
        assertTrue(result.data?.isNotEmpty() == true) // 确保用户有文章
    }

    @Test
    fun testGetUserArticlesLikedSavedSuccess() {
        val userId = 2L // 假设用户ID为2
        val categories = listOf("liked", "saved") // 分类列表，包含“liked”和“saved”


        // 遍历分类列表
        for (category in categories) {
            // 调用 getArticlesByCategoryAndUserId 方法
            val result = articleServiceImpl.getArticlesByCategoryAndUserId(category, userId)

            // 打印结果以 JSON 格式
            println("Category: $category")
            println(objectMapper.writeValueAsString(result))

            // 验证返回结果，确保没有找到文章
            assertTrue(result.isSuccess)
            assertTrue(result.data?.isNotEmpty() == true) // 确保用户有文章
        }
    }

    @Test
    fun testGetUserArticlesLikedSavedFail() {
        val userId = 1L // 假设用户ID为1
        val categories = listOf("liked", "saved") // 分类列表，包含“liked”和“saved”

        // 遍历分类列表
        for (category in categories) {
            // 调用 getArticlesByCategoryAndUserId 方法
            val result = articleServiceImpl.getArticlesByCategoryAndUserId(category, userId)

            // 打印结果以 JSON 格式
            println("Category: $category")
            println(objectMapper.writeValueAsString(result))

            // 验证返回结果，确保没有找到文章
            assertTrue(!result.isSuccess)
            assertNull(result.data)
        }
    }

    @Test
    fun testGetHotArticles() {
        val result = articleServiceImpl.getHotArticle()

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果
        assertTrue(result.isSuccess)
        assertTrue(result.data?.isNotEmpty() == true) // 确保找到文章
    }

    @Test
    fun testSearchArticleByKeyword() {
        val keyword = "测试标题"
        val page = 1
        val pageSize = 5

        // 调用 searchArticleByKeyword 方法
        val result = articleServiceImpl.searchArticleByKeyword(keyword, page, pageSize)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证返回结果，确保能根据关键字找到文章
        assertTrue(result.isSuccess)
        assertTrue(result.data?.isNotEmpty() == true) // 确保找到文章
    }

    @Test
    fun testUpdateArticleById() {
        val articleId = testArticleId
        val articleDTO = ArticleDTO(userId = 1, title = "更新标题", content = "更新内容")

        // 调用 updateArticleById 方法
        val result = articleServiceImpl.updateArticleById(articleDTO, articleId)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证文章是否更新成功
        assertTrue(result.isSuccess)
        assertEquals("更新标题", result.data?.title) // 确保标题已更新
        assertEquals("更新内容", result.data?.content) // 确保内容已更新
    }

    @Test
    fun testUpdateArticleStatusById() {
        val userId = 1L // 假设用户 ID 为 1
        val articleId = testArticleId
        val aspects = listOf("likes", "saves") // 定义aspect的值
        val ops = listOf("incr", "decr") // 定义op的值

        // 使用循环遍历aspect和op的组合
        for (aspect in aspects) {
            for (op in ops) {
                // 调用 updateArticleStatusById 方法
                val result = articleServiceImpl.updateArticleStatusById(userId, articleId, aspect, op)

                // 打印结果以 JSON 格式
                println("aspect: $aspect, op: $op")
                println(objectMapper.writeValueAsString(result))
            }
        }
    }

    @Test
    fun testDeleteArticleById() {
        val articleId = 1L // 假设 ID 为 1 的文章存在

        // 调用 deleteArticleById 方法
        val result = articleServiceImpl.deleteArticleById(articleId)

        // 打印结果以 JSON 格式
        println(objectMapper.writeValueAsString(result))

        // 验证删除操作是否成功
        assertTrue(!result.isSuccess)
    }
}
