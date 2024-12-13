package com.example.main.service.blog.implement

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import com.example.main.Response
import com.example.main.converter.blog.ArticleConverter
import com.example.main.dao.blog.*
import com.example.main.dao.blog.repository.ArticleRepository
import com.example.main.dao.blog.repository.LikedArticleRepository
import com.example.main.dao.blog.repository.SavedArticleRepository
import com.example.main.dao.login.ParentRepository
import com.example.main.dto.blog.ArticleDTO
import com.example.main.service.blog.ArticleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
open class ArticleServiceImpl : ArticleService {

    @Autowired
    private lateinit var savedArticleRepository: SavedArticleRepository

    @Autowired
    private lateinit var likedArticleRepository: LikedArticleRepository

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var userRepository: ParentRepository

    @Autowired
    private lateinit var elasticsearchClient: ElasticsearchClient

    // 根据文章ID获取文章
    override fun getArticleById(id: Long): Response<Article> {
        return try {
            val article = articleRepository.findById(id)
            if (article.isPresent) {
                Response.newSuccess(article.get())
            } else {
                Response.newFail("未找到ID为 $id 的文章")
            }
        } catch (e: Exception) {
            Response.newFail("获取ID为 $id 的文章失败 - ${e.message}")
        }
    }

    // 根据用户ID和类别获取相应的文章列表
    override fun getArticlesByCategoryAndUserId(category: String, userId: Long): Response<List<Article>> {
        return try {
            // 根据category判断是获取用户的文章还是用户点赞的文章
            when (category) {
                "user" -> {
                    // 获取该用户的所有文章
                    val articles = articleRepository.findByUserId(userId)
                    if (articles.isNotEmpty()) {
                        Response.newSuccess(articles)
                    } else {
                        Response.newFail("用户ID为 $userId 的文章不存在")
                    }
                }

                "liked" -> {
                    // 获取该用户所有的点赞记录
                    val likedArticles = likedArticleRepository.findById_UserId(userId)

                    // 如果没有找到点赞记录，返回失败信息
                    if (likedArticles.isEmpty()) {
                        return Response.newFail("该用户未点赞任何文章")
                    }

                    // 获取该用户点赞的文章ID
                    val likedArticleIds = likedArticles.map { it.id.articleId }

                    // 根据文章ID获取所有相关的文章详细信息
                    val articles = articleRepository.findAllById(likedArticleIds)

                    // 如果根据ID找不到任何文章，返回失败信息
                    if (articles.isEmpty()) {
                        return Response.newFail("未找到用户ID为 $userId 点赞的文章")
                    }

                    // 返回成功并提供点赞的文章列表
                    Response.newSuccess(articles)
                }

                "saved" -> {
                    // 获取该用户所有的收藏记录
                    val savedArticles = savedArticleRepository.findById_UserId(userId)

                    // 如果没有找到收藏记录，返回失败信息
                    if (savedArticles.isEmpty()) {
                        return Response.newFail("该用户未收藏任何文章")
                    }

                    // 获取该用户收藏的文章ID
                    val savedArticleIds = savedArticles.map { it.id.articleId }

                    // 根据文章ID获取所有相关的文章详细信息
                    val articles = articleRepository.findAllById(savedArticleIds)

                    // 如果根据ID找不到任何文章，返回失败信息
                    if (articles.isEmpty()) {
                        return Response.newFail("未找到用户ID为 $userId 收藏的文章")
                    }

                    // 返回成功并提供点赞的文章列表
                    Response.newSuccess(articles)
                }

                else -> {
                    // 如果传入的category无效
                    Response.newFail("无效的类别: $category")
                }
            }
        } catch (e: Exception) {
            // 捕获异常并返回失败信息
            Response.newFail("获取用户ID为 $userId 的文章失败 - ${e.message}")
        }
    }

    // 根据关键词搜索文章
    override fun searchArticleByKeyword(keyword: String, page: Int, pageSize: Int): Response<List<Article>> {
        return try {
            // 计算分页参数
            val from = (page - 1) * pageSize

            // 构建 Elasticsearch 查询请求
            val searchRequest = SearchRequest.Builder()
                .index("article") // 索引名称
                .query { q ->
                    q.multiMatch { mm ->
                        mm.query(keyword) // 搜索关键词
                            .fields("title^2", "content") // title 字段权重更高
                    }
                }
                .from(from) // 分页起始位置
                .size(pageSize) // 每页返回条目数
                .sort { s ->
                    s.field { f ->
                        f.field("timeCreated") // 按创建时间排序
                            .order(SortOrder.Desc) // 降序排列
                    }
                }
                .build()

            // 执行搜索请求
            val response = elasticsearchClient.search(searchRequest, ArticleSearch::class.java)

            // 获取文档 ID 列表并转换为 Long
            val articleIds = response.hits().hits().mapNotNull { it.id()?.toLongOrNull() }

            // 根据 ID 从 MySQL 获取文章数据
            if (articleIds.isNotEmpty()) {
                val articles = articleRepository.findAllById(articleIds).sortedBy { articleIds.indexOf(it.articleId) }

                if (articles.isNotEmpty()) {
                    Response.newSuccess(articles)
                } else {
                    Response.newFail("未找到包含关键词 '$keyword' 的文章")
                }
            } else {
                Response.newFail("未找到包含关键词 '$keyword' 的文章")
            }
        } catch (e: Exception) {
            Response.newFail("搜索文章失败 - ${e.message}")
        }
    }

    // 根据点赞数和收藏数总和降序排列获得最热文章
    override fun getHotArticle(): Response<List<Article>> {
        return try {
            // 获取所有文章的列表
            val articles = articleRepository.findAll()

            // 使用 sortedWith 进行多条件排序
            val sortedArticles = articles.sortedWith(
                compareByDescending<Article> { it.likes + it.saves } // 按点赞数和收藏数总和降序排列
                    .thenByDescending { it.articleId }  // 如果总和相同，再按文章ID降序排列
            )

            // 限制返回前10个文章
            val topArticles = sortedArticles.take(10)

            // 返回排序后的前10篇文章
            Response.newSuccess(topArticles)
        } catch (e: Exception) {
            // 捕获异常并返回失败响应
            Response.newFail("Failed to fetch or sort articles: ${e.message}")
        }
    }

    // 创建文章，同时将文章数据存入ES
    override fun createArticle(articleDTO: ArticleDTO): Response<Long?> {
        val article: Article = ArticleConverter.convertArticle(articleDTO)
        return try {
            val userOptional = userRepository.findById(article.userId)

            if (userOptional.isPresent) {
                val user = userOptional.get()
                article.username = user.name // 将用户名赋值给文章
                val savedArticle = articleRepository.save(article)

                // 存入Elasticsearch
                val articleSearch = ArticleSearch(
                    articleId = article.articleId,
                    title = article.title,
                    content = article.content,
                    timeCreated = article.time
                )
                val request: IndexRequest<ArticleSearch> = IndexRequest.of { i ->
                    i.index("article").id("${article.articleId}").document(articleSearch)
                }

                elasticsearchClient.index(request) // 保存到ES

                Response.newSuccess(savedArticle.articleId)
            } else {
                Response.newFail("未找到ID为 ${article.userId} 的用户")
            }
        } catch (e: Exception) {
            Response.newFail("创建文章失败 - ${e.message}")
        }
    }

    // 根据文章ID删除Mysql和ES文章
    override fun deleteArticleById(id: Long): Response<Void> {
        return try {
            articleRepository.findById(id).orElseThrow {
                IllegalArgumentException("ID为 $id 的文章不存在")
            }
            articleRepository.deleteById(id) // 删除文章

            // 删除Elasticsearch中对应的文章
            elasticsearchClient.delete { del ->
                del.index("article").id("$id")
            }

            Response.newSuccess(null)
        } catch (e: Exception) {
            Response.newFail("删除ID为 $id 的文章失败 - ${e.message}")
        }
    }

    // 更新Mysql和ES中的文章
    @Transactional
    override fun updateArticleById(articleDTO: ArticleDTO, articleId: Long): Response<Article> {
        return try {
            // 从数据库中获取旧文章数据
            val articleOptional = articleRepository.findById(articleId)

            if (articleOptional.isPresent) {
                val article = articleOptional.get() // 获取旧文章

                // 使用 articleDTO 中的新数据更新文章字段
                articleDTO.title?.let { article.title = it }
                articleDTO.content?.let { article.content = it }
                article.time = LocalDateTime.now() // 更新文章时间

                // 将更新后的文章保存到数据库
                val updatedArticle = articleRepository.save(article)

                // 正确格式化日期
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val timeCreatedFormatted = updatedArticle.time.format(formatter)

                // 定义要更新到 Elasticsearch 的字段
                val updatedArticleSearch = mapOf(
                    "title" to updatedArticle.title, // 更新后的标题
                    "content" to updatedArticle.content, // 更新后的内容
                    "timeCreated" to timeCreatedFormatted // 正确格式化的时间
                )

                // 将更新后的文章同步到 Elasticsearch
                elasticsearchClient.update<ArticleSearch, Map<String, Any>>(
                    { upd ->
                        upd.index("article") // 指定索引名称
                            .id("${updatedArticle.articleId}") // 指定文档 ID
                            .doc(updatedArticleSearch) // 部分文档更新字段
                    },
                    ArticleSearch::class.java // 明确指定文档的类型
                )

                // 返回更新后的文章作为成功响应
                Response.newSuccess(updatedArticle)
            } else {
                // 如果文章不存在，返回失败响应
                Response.newFail("未找到ID为 $articleId 的文章")
            }
        } catch (e: Exception) {
            // 捕获异常并返回失败响应
            Response.newFail("更新ID为 $articleId 的文章失败 - ${e.message}")
        }
    }

    // 更新文章状态，例如点赞数或收藏数
    @Transactional
    override fun updateArticleStatusById(userId: Long, articleId: Long, aspect: String, op: String): Response<Article> {
        return try {
            // 查找文章
            val articleOptional = articleRepository.findById(articleId)

            // 如果文章存在，继续执行
            if (articleOptional.isPresent) {
                val article = articleOptional.get()

                // 根据操作类型更新文章的状态
                when (aspect) {
                    // 更新点赞数
                    "likes" -> {
                        // 检查用户是否已经点赞此文章
                        val likedArticleId = LikedArticleId(userId = userId, articleId = articleId)
                        val existingLike = likedArticleRepository.findById(likedArticleId)

                        when (op) {
                            // 增加点赞数
                            "incr" -> {
                                // 如果用户已经点赞，返回失败
                                if (existingLike.isPresent) {
                                    return Response.newFail("用户已点赞此文章")
                                }
                                // 增加点赞数，并记录该用户点赞该文章
                                article.likes++
                                likedArticleRepository.save(LikedArticle(id = likedArticleId))
                            }
                            // 减少点赞数
                            "decr" -> {
                                // 如果没有找到点赞记录，返回失败
                                if (!existingLike.isPresent) {
                                    return Response.newFail("用户尚未点赞此文章，无法取消点赞")
                                }

                                // 减少点赞数，并删除该用户的点赞记录
                                article.likes--
                                likedArticleRepository.deleteById_UserIdAndId_ArticleId(userId, articleId)
                            }

                            else -> return Response.newFail("点赞操作无效: $op")
                        }
                    }
                    // 更新收藏数
                    "saves" -> {
                        // 检查用户是否已经收藏此文章
                        val savedArticleId = SavedArticleId(userId = userId, articleId = articleId)
                        val existingSave = savedArticleRepository.findById(savedArticleId)

                        when (op) {
                            // 增加收藏数
                            "incr" -> {
                                // 如果用户已收藏，返回失败
                                if (existingSave.isPresent) {
                                    return Response.newFail("用户已收藏此文章")
                                }
                                // 增加收藏数，并记录该用户收藏该文章
                                article.saves++
                                savedArticleRepository.save(SavedArticle(id = savedArticleId))
                            }
                            // 减少收藏数
                            "decr" -> {
                                // 如果没有找到收藏记录，返回失败
                                if (!existingSave.isPresent) {
                                    return Response.newFail("用户尚未收藏此文章，无法取消收藏")
                                }

                                // 减少收藏数，并删除该用户的收藏记录
                                article.saves--
                                savedArticleRepository.deleteById_UserIdAndId_ArticleId(userId, articleId)
                            }

                            else -> return Response.newFail("收藏操作无效: $op")
                        }
                    }
                    // 如果 aspect 参数无效
                    else -> return Response.newFail("无效的状态字段: $aspect")
                }

                // 保存更新后的文章
                val updatedArticle = articleRepository.save(article)
                Response.newSuccess(updatedArticle)
            } else {
                // 如果未找到文章，返回失败
                Response.newFail("未找到ID为 $articleId 的文章")
            }
        } catch (e: Exception) {
            // 处理异常，返回失败信息
            Response.newFail("更新ID为 $articleId 的文章状态失败 - ${e.message}")
        }
    }
}