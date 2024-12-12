package com.example.main

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient
import co.elastic.clients.elasticsearch.indices.PutMappingRequest
import co.elastic.clients.elasticsearch.indices.PutMappingResponse
import com.example.main.dao.blog.ArticleSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
class ArticleElasticSearchTests {

    @Autowired
    private lateinit var elasticsearchClient: ElasticsearchClient

    @Test
    @Throws(IOException::class)
    fun testCreateMapping() {
        // 定义索引的映射
        val mappingRequest = PutMappingRequest.Builder()
            .index("article_test") // 索引的名称
            .properties("articleId") { prop ->
                prop.long_ { it } // 定义 `articleId` 为 `long` 类型
            }
            .properties("title") { prop ->
                prop.text { it } // 定义 `title` 为 `text` 类型
            }
            .properties("content") { prop ->
                prop.text { it } // 定义 `content` 为 `text` 类型
            }
            .properties("timeCreated") { prop ->
                prop.date { dateBuilder ->
                    dateBuilder.format("yyyy-MM-dd HH:mm:ss") // 设置自定义日期格式
                }
            }
            .build()

        // 获取 Elasticsearch 索引客户端
        val indices: ElasticsearchIndicesClient = elasticsearchClient.indices()

        // 检查索引是否存在，如果存在则删除
        if (indices.exists { it.index("article_test") }.value()) {
            indices.delete { it.index("article_test") }
            println("索引 'article_test' 删除成功。")
        } else {
            println("索引 'article_test' 不存在。")
        }

        // 创建一个新索引
        indices.create { it.index("article_test") }
        println("索引 'article_test' 创建成功。")

        // 将映射应用到索引
        val response: PutMappingResponse = indices.putMapping(mappingRequest)
        println(response)

        // 确认映射的响应
        if (response.acknowledged()) {
            println("索引 'article_test' 的映射创建成功。")
        } else {
            println("索引 'article_test' 的映射创建失败。")
        }
    }

    @Test
    @Throws(IOException::class)
    fun testAddData() {
        // 创建文章
        val articles = listOf(
            Pair(
                "1", ArticleSearch(
                    articleId = 1,
                    title = "异世界登录第一天！",
                    content = "新世界我来辣！",
                    timeCreated = LocalDateTime.now()
                )
            ),
            Pair(
                "2", ArticleSearch(
                    articleId = 2,
                    title = "异世界登录第二天！",
                    content = "新世界我来辣！",
                    timeCreated = LocalDateTime.now()
                )
            ),
            Pair(
                "3", ArticleSearch(
                    articleId = 3,
                    title = "异世界登录第三天！",
                    content = "新世界我来辣！等等。。。我是不是说第三次了？",
                    timeCreated = LocalDateTime.now()
                )
            ),
            Pair(
                "4", ArticleSearch(
                    articleId = 4,
                    title = "异世界登录第四天！",
                    content = "新世界我来辣！可恶！得想办法，这个躯体还是太局限了！",
                    timeCreated = LocalDateTime.now()
                )
            )
        )

        // 遍历文章并索引它们
        for ((id, article) in articles) {
            val request = IndexRequest.of { i ->
                i.index("article_test")
                    .id(id) // 设置文章 ID
                    .document(article) // 设置文章为文档
            }

            val response = elasticsearchClient.index(request) // 发送索引请求
            println("文章已索引，ID: $id, 结果: ${response.result()}") // 记录结果
        }
    }

    @Test
    @Throws(IOException::class)
    fun testDeleteData() {
        val articleIdToDelete = "2"

        // 删除前检查文档是否存在
        val exists = elasticsearchClient.exists { req ->
            req.index("article_test").id(articleIdToDelete)
        }

        if (exists.value()) {
            val response = elasticsearchClient.delete { del ->
                del.index("article_test").id(articleIdToDelete)
            }
            println("文章已删除，ID: $articleIdToDelete, 结果: ${response.result()}")
        } else {
            println("文章 ID: $articleIdToDelete 不存在。")
        }
    }

    @Test
    @Throws(IOException::class)
    fun testUpdateData() {
        val articleIdToUpdate: Long = 3

        // 更新前检查文档是否存在
        val exists = elasticsearchClient.exists { req ->
            req.index("article_test").id("$articleIdToUpdate")
        }

        if (exists.value()) {
            // 正确格式化日期
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val timeCreatedFormatted = LocalDateTime.now().format(formatter)

            // 定义更新的字段
            val updatedArticle = mapOf(
                "title" to "异世界登录第三天更新！",
                "content" to "新内容，新的异世界探索！",
                "timeCreated" to timeCreatedFormatted // 正确格式化的日期
            )

            // 使用显式类型更新
            val response = elasticsearchClient.update<ArticleSearch, Map<String, Any>>(
                { upd ->
                    upd.index("article_test")
                        .id("$articleIdToUpdate")
                        .doc(updatedArticle) // 部分文档更新字段
                },
                ArticleSearch::class.java // 明确指定文档的类型
            )

            println("文章已更新，ID: $articleIdToUpdate, 结果: ${response.result()}")
        } else {
            println("文章 ID: $articleIdToUpdate 不存在。")
        }
    }

    @Test
    @Throws(IOException::class)
    fun testSearch() {
        val queryKeyword: String = "世界"

        try {
            // 构建搜索请求，优先匹配标题包含“宝宝”的文档，其次是内容包含“宝宝”的文档，并按创建时间降序排序
            val searchRequest = SearchRequest.Builder()
                .index("article_test") // 指定索引名称
                .query { q ->
                    q.bool { b ->
                        b.should {
                            it.match { m ->
                                m.field("title") // 优先搜索标题字段
                                    .query(queryKeyword)
                            }
                        }
                        b.should {
                            it.match { m ->
                                m.field("content") // 其次搜索内容字段
                                    .query(queryKeyword)
                            }
                        }
                    }
                }
                .sort { s ->
                    s.field { f ->
                        f.field("timeCreated") // 按创建时间排序
                            .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc) // 降序
                    }
                }
                .build()

            // 执行搜索请求
            val response = elasticsearchClient.search(searchRequest, ArticleSearch::class.java)

            // 获取搜索结果中的文档
            val hits = response.hits().hits()
            if (hits.isEmpty()) {
                println("未找到包含关键词 '$queryKeyword' 的文章。") // 如果没有结果，打印提示信息
            } else {
                hits.forEach { hit ->
                    val article = hit.source() // 反序列化文档为 `ArticleSearch` 对象
                    println("找到文章: 标题=${article?.title}, 内容=${article?.content}, 时间=${article?.timeCreated}")
                }
            }
        } catch (e: Exception) {
            println("搜索操作失败: ${e.message}") // 捕获异常并打印错误信息
        }
    }
}
