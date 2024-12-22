package com.example.main.service.blog.implement

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.core.SearchRequest
import com.example.main.Response
import com.example.main.converter.blog.QnAConverter
import com.example.main.dao.blog.*
import com.example.main.dao.blog.repository.QnARepository
import com.example.main.dao.blog.repository.LikedQnARepository
import com.example.main.dao.blog.repository.SavedQnARepository
import com.example.main.dao.login.ParentRepository
import com.example.main.dto.blog.QnADTO
import com.example.main.service.blog.QnAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
open class QnAServiceImpl : QnAService {

    @Autowired
    private lateinit var savedQnARepository: SavedQnARepository

    @Autowired
    private lateinit var likedQnARepository: LikedQnARepository

    @Autowired
    private lateinit var qnaRepository: QnARepository

    @Autowired
    private lateinit var userRepository: ParentRepository

    @Autowired
    private lateinit var elasticsearchClient: ElasticsearchClient

    // 根据问贴ID获取问贴
    override fun getQnAById(id: Long): Response<QnA> {
        return try {
            val qna = qnaRepository.findById(id)
            if (qna.isPresent) {
                Response.newSuccess(qna.get())
            } else {
                Response.newFail("未找到ID为 $id 的问贴")
            }
        } catch (e: Exception) {
            Response.newFail("获取ID为 $id 的问贴失败 - ${e.message}")
        }
    }

    // 根据用户ID和类别获取相应的问贴列表
    override fun getQnAsByCategoryAndUserId(category: String, userId: Long): Response<List<QnA>> {
        return try {
            // 根据category判断是获取用户的问贴还是用户点赞的问贴
            when (category) {
                "user" -> {
                    // 获取该用户的所有问贴
                    val qnas = qnaRepository.findByUserId(userId)
                    if (qnas.isNotEmpty()) {
                        Response.newSuccess(qnas)
                    } else {
                        Response.newFail("用户ID为 $userId 的问贴不存在")
                    }
                }

                "liked" -> {
                    // 获取该用户所有的点赞记录
                    val likedQnAs = likedQnARepository.findById_UserId(userId)

                    // 如果没有找到点赞记录，返回失败信息
                    if (likedQnAs.isEmpty()) {
                        return Response.newFail("该用户未点赞任何问贴")
                    }

                    // 获取该用户点赞的问贴ID
                    val likedQnAIds = likedQnAs.map { it.id.qnaId }

                    // 根据问贴ID获取所有相关的问贴详细信息
                    val qnas = qnaRepository.findAllById(likedQnAIds)

                    // 如果根据ID找不到任何问贴，返回失败信息
                    if (qnas.isEmpty()) {
                        return Response.newFail("未找到用户ID为 $userId 点赞的问贴")
                    }

                    // 返回成功并提供点赞的问贴列表
                    Response.newSuccess(qnas)
                }

                "saved" -> {
                    // 获取该用户所有的收藏记录
                    val savedQnAs = savedQnARepository.findById_UserId(userId)

                    // 如果没有找到收藏记录，返回失败信息
                    if (savedQnAs.isEmpty()) {
                        return Response.newFail("该用户未收藏任何问贴")
                    }

                    // 获取该用户收藏的问贴ID
                    val savedQnAIds = savedQnAs.map { it.id.qnaId }

                    // 根据问贴ID获取所有相关的问贴详细信息
                    val qnas = qnaRepository.findAllById(savedQnAIds)

                    // 如果根据ID找不到任何问贴，返回失败信息
                    if (qnas.isEmpty()) {
                        return Response.newFail("未找到用户ID为 $userId 收藏的问贴")
                    }

                    // 返回成功并提供点赞的问贴列表
                    Response.newSuccess(qnas)
                }

                else -> {
                    // 如果传入的category无效
                    Response.newFail("无效的类别: $category")
                }
            }
        } catch (e: Exception) {
            // 捕获异常并返回失败信息
            Response.newFail("获取用户ID为 $userId 的问贴失败 - ${e.message}")
        }
    }

    // 根据关键词搜索问贴
    override fun searchQnAByKeyword(keyword: String, page: Int, pageSize: Int): Response<List<QnA>> {
        // 记录方法开始时间
        val startTime = System.currentTimeMillis()

        return try {
            // 计算分页参数
            val from = (page - 1) * pageSize

            // 构建 Elasticsearch 查询请求
            val searchRequest = SearchRequest.Builder()
                .index("qna") // 索引名称
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
            val response = elasticsearchClient.search(searchRequest, QnASearch::class.java)

            // 获取文档 ID 列表并转换为 Long
            val qnaIds = response.hits().hits().mapNotNull { it.id()?.toLongOrNull() }

            // 根据 ID 从 MySQL 获取问贴数据
            if (qnaIds.isNotEmpty()) {
                val qnas = qnaRepository.findAllById(qnaIds).sortedBy { qnaIds.indexOf(it.qnaId) }

                if (qnas.isNotEmpty()) {
                    // 记录结束时间并计算总耗时
                    val endTime = System.currentTimeMillis()
                    val elapsedTime = endTime - startTime

                    // 打印执行时间（你可以根据需要打印或记录到日志中）
                    println("searchQnAByKeyword took $elapsedTime ms")

                    Response.newSuccess(qnas)
                } else {
                    Response.newFail("未找到包含关键词 '$keyword' 的问贴")
                }
            } else {
                Response.newFail("未找到包含关键词 '$keyword' 的问贴")
            }
        } catch (e: Exception) {
            // 记录结束时间并计算总耗时
            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime

            // 打印执行时间（你可以根据需要打印或记录到日志中）
            println("searchQnAByKeyword failed after $elapsedTime ms with error: ${e.message}")

            Response.newFail("搜索问贴失败 - ${e.message}")
        }
    }

    // 根据点赞数和收藏数总和降序排列获得最热问贴
    override fun getHotQnA(): Response<List<QnA>> {
        return try {
            // 获取所有问贴的列表
            val qnas = qnaRepository.findAll()

            // 使用 sortedWith 进行多条件排序
            val sortedQnAs = qnas.sortedWith(
                compareByDescending<QnA> { it.likes + it.saves } // 按点赞数和收藏数总和降序排列
                    .thenByDescending { it.qnaId }  // 如果总和相同，再按问贴ID降序排列
            )

            // 限制返回前10个问贴
            val topQnAs = sortedQnAs.take(10)

            // 返回排序后的前10篇问贴
            Response.newSuccess(topQnAs)
        } catch (e: Exception) {
            // 捕获异常并返回失败响应
            Response.newFail("Failed to fetch or sort qnas: ${e.message}")
        }
    }

    // 创建问贴，同时将问贴数据存入ES
    override fun createQnA(qnaDTO: QnADTO): Response<Long?> {
        val qna: QnA = QnAConverter.convertQnA(qnaDTO)
        return try {
            val userOptional = userRepository.findById(qna.userId)

            if (userOptional.isPresent) {
                val user = userOptional.get()
                qna.username = user.name // 将用户名赋值给问贴
                val savedQnA = qnaRepository.save(qna)

                // 存入Elasticsearch
                val qnaSearch = QnASearch(
                    qnaId = qna.qnaId,
                    title = qna.title,
                    content = qna.content,
                    timeCreated = qna.time
                )
                val request: IndexRequest<QnASearch> = IndexRequest.of { i ->
                    i.index("qna").id("${qna.qnaId}").document(qnaSearch)
                }

                elasticsearchClient.index(request) // 保存到ES

                Response.newSuccess(savedQnA.qnaId)
            } else {
                Response.newFail("未找到ID为 ${qna.userId} 的用户")
            }
        } catch (e: Exception) {
            Response.newFail("创建问贴失败 - ${e.message}")
        }
    }

    // 根据问贴ID删除Mysql和ES问贴
    override fun deleteQnAById(id: Long): Response<Void> {
        return try {
            qnaRepository.findById(id).orElseThrow {
                IllegalArgumentException("ID为 $id 的问贴不存在")
            }
            qnaRepository.deleteById(id) // 删除问贴

            // 删除Elasticsearch中对应的问贴
            elasticsearchClient.delete { del ->
                del.index("qna").id("$id")
            }

            Response.newSuccess(null)
        } catch (e: Exception) {
            Response.newFail("删除ID为 $id 的问贴失败 - ${e.message}")
        }
    }

    // 更新Mysql和ES中的问贴
    @Transactional
    override fun updateQnAById(qnaDTO: QnADTO, qnaId: Long): Response<QnA> {
        return try {
            // 从数据库中获取旧问贴数据
            val qnaOptional = qnaRepository.findById(qnaId)

            if (qnaOptional.isPresent) {
                val qna = qnaOptional.get() // 获取旧问贴

                // 使用 qnaDTO 中的新数据更新问贴字段
                qnaDTO.title?.let { qna.title = it }
                qnaDTO.content?.let { qna.content = it }
                qna.time = LocalDateTime.now() // 更新问贴时间

                // 将更新后的问贴保存到数据库
                val updatedQnA = qnaRepository.save(qna)

                // 正确格式化日期
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val timeCreatedFormatted = updatedQnA.time.format(formatter)

                // 定义要更新到 Elasticsearch 的字段
                val updatedQnASearch = mapOf(
                    "title" to updatedQnA.title, // 更新后的标题
                    "content" to updatedQnA.content, // 更新后的内容
                    "timeCreated" to timeCreatedFormatted // 正确格式化的时间
                )

                // 将更新后的问贴同步到 Elasticsearch
                elasticsearchClient.update<QnASearch, Map<String, Any>>(
                    { upd ->
                        upd.index("qna") // 指定索引名称
                            .id("${updatedQnA.qnaId}") // 指定文档 ID
                            .doc(updatedQnASearch) // 部分文档更新字段
                    },
                    QnASearch::class.java // 明确指定文档的类型
                )

                // 返回更新后的问贴作为成功响应
                Response.newSuccess(updatedQnA)
            } else {
                // 如果问贴不存在，返回失败响应
                Response.newFail("未找到ID为 $qnaId 的问贴")
            }
        } catch (e: Exception) {
            // 捕获异常并返回失败响应
            Response.newFail("更新ID为 $qnaId 的问贴失败 - ${e.message}")
        }
    }

    // 更新问贴状态，例如点赞数或收藏数
    @Transactional
    override fun updateQnAStatusById(userId: Long, qnaId: Long, aspect: String, op: String): Response<QnA> {
        return try {
            // 查找问贴
            val qnaOptional = qnaRepository.findById(qnaId)

            // 如果问贴存在，继续执行
            if (qnaOptional.isPresent) {
                val qna = qnaOptional.get()

                // 根据操作类型更新问贴的状态
                when (aspect) {
                    // 更新点赞数
                    "likes" -> {
                        // 检查用户是否已经点赞此问贴
                        val likedQnAId = LikedQnAId(userId = userId, qnaId = qnaId)
                        val existingLike = likedQnARepository.findById(likedQnAId)

                        when (op) {
                            // 增加点赞数
                            "incr" -> {
                                // 如果用户已经点赞，返回失败
                                if (existingLike.isPresent) {
                                    return Response.newFail("用户已点赞此问贴")
                                }
                                // 增加点赞数，并记录该用户点赞该问贴
                                qna.likes++
                                likedQnARepository.save(LikedQnA(id = likedQnAId))
                            }
                            // 减少点赞数
                            "decr" -> {
                                // 如果没有找到点赞记录，返回失败
                                if (!existingLike.isPresent) {
                                    return Response.newFail("用户尚未点赞此问贴，无法取消点赞")
                                }

                                // 减少点赞数，并删除该用户的点赞记录
                                qna.likes--
                                likedQnARepository.deleteById_UserIdAndId_QnaId(userId, qnaId)
                            }

                            else -> return Response.newFail("点赞操作无效: $op")
                        }
                    }
                    // 更新收藏数
                    "saves" -> {
                        // 检查用户是否已经收藏此问贴
                        val savedQnAId = SavedQnAId(userId = userId, qnaId = qnaId)
                        val existingSave = savedQnARepository.findById(savedQnAId)

                        when (op) {
                            // 增加收藏数
                            "incr" -> {
                                // 如果用户已收藏，返回失败
                                if (existingSave.isPresent) {
                                    return Response.newFail("用户已收藏此问贴")
                                }
                                // 增加收藏数，并记录该用户收藏该问贴
                                qna.saves++
                                savedQnARepository.save(SavedQnA(id = savedQnAId))
                            }
                            // 减少收藏数
                            "decr" -> {
                                // 如果没有找到收藏记录，返回失败
                                if (!existingSave.isPresent) {
                                    return Response.newFail("用户尚未收藏此问贴，无法取消收藏")
                                }

                                // 减少收藏数，并删除该用户的收藏记录
                                qna.saves--
                                savedQnARepository.deleteById_UserIdAndId_QnaId(userId, qnaId)
                            }

                            else -> return Response.newFail("收藏操作无效: $op")
                        }
                    }
                    // 如果 aspect 参数无效
                    else -> return Response.newFail("无效的状态字段: $aspect")
                }

                // 保存更新后的问贴
                val updatedQnA = qnaRepository.save(qna)
                Response.newSuccess(updatedQnA)
            } else {
                // 如果未找到问贴，返回失败
                Response.newFail("未找到ID为 $qnaId 的问贴")
            }
        } catch (e: Exception) {
            // 处理异常，返回失败信息
            Response.newFail("更新ID为 $qnaId 的问贴状态失败 - ${e.message}")
        }
    }
}