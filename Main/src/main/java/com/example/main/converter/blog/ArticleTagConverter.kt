package com.example.LoginRegister.converter.blog

import com.example.demo.dao.ArticleTag
import com.example.demo.dao.ArticleTagList
import com.example.demo.dto.ArticleTagDTO

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