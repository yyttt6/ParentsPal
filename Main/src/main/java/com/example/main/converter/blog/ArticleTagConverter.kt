package com.example.main.converter.blog

import com.example.main.dao.blog.ArticleTag
import com.example.main.dao.blog.ArticleTagList
import com.example.main.dto.blog.ArticleTagDTO

class ArticleTagConverter {
    companion object{
//        fun convertArticleTag (student: ArticleTag): ArticleTagDTO {
//            return ArticleTagDTO(student.id, student.name, student.email, student.age)
//        }

        fun convertArticleTag (articleDTO: ArticleTagDTO): ArticleTag {
            return ArticleTag(null, articleDTO.tagString)
        }

        fun convertTagToTagList (articleDTO: ArticleTagDTO): ArticleTagList{
            return ArticleTagList(null, articleDTO.articleId, articleDTO.articleTagId)
        }
    }
}