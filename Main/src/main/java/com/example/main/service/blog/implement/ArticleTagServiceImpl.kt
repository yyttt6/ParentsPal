package com.example.main.service.blog.implement

import com.example.main.converter.blog.ArticleTagConverter.Companion.convertArticleTag
import com.example.main.dao.blog.ArticleTag
import com.example.main.dao.blog.repository.ArticleTagListRepository
import com.example.main.dao.blog.repository.ArticleTagRepository
import com.example.main.dto.blog.ArticleTagDTO
import com.example.main.service.blog.ArticleTagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class ArticleTagServiceImpl: ArticleTagService {
    @Autowired
    private lateinit var articleTagListRepository: ArticleTagListRepository

    @Autowired
    private lateinit var articleTagRepository: ArticleTagRepository

    override fun getArticleTagById(id: Long): ArticleTag {
        return articleTagRepository.findById(id).orElseThrow { RuntimeException("ArticleTag not found with id: $id") }
    }

    override fun createArticleTag(articleTagDTO: ArticleTagDTO): Long? {
        // Check if a tag with the same name already exists
        val articleTag = convertArticleTag(articleTagDTO)
        val existingTag = articleTagRepository.findByName(articleTag.name)

        // Assign tagId based on whether the tag already exists or not
        val tagId: Long? = if (existingTag != null) {
            // If tag exists, use the existing tag's ID
            existingTag.id
        } else {
            // If tag does not exist, save the new tag and return its ID
            val savedArticleTag = articleTagRepository.save(articleTag)
            savedArticleTag.id
        }

        val articleTagList =
        articleTagListRepository.save(articleTag)

        return tagId
    }




    override fun deleteArticleTagById(id: Long) {
        articleTagRepository.findById(id).orElseThrow {
            IllegalArgumentException("id: $id doesn't exist!")
        }
        articleTagRepository.deleteById(id)
    }

    @Transactional
    override fun updateArticleTagById(id: Long, name: String): ArticleTag {
        // 寻找帖子id
        val articleTagOptional: Optional<ArticleTag> = articleTagRepository.findById(id)

        // 查看帖子是否存在
        if (articleTagOptional.isPresent) {
            val articleTag = articleTagOptional.get()

            articleTag.name = name

            val savedArticleTag: ArticleTag = articleTagRepository.save(articleTag)
            return savedArticleTag

        } else {
            // 不存在则报错
            throw NoSuchElementException("No articleTag found with id: $id")
        }
    }
}