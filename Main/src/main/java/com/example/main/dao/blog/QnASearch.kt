package com.example.main.dao.blog

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class QnASearch(
    @JsonProperty("qnaId")
    val qnaId: Long? = null,

    @JsonProperty("title")
    val title: String? = null,

    @JsonProperty("content")
    val content: String? = null,

    @JsonProperty("timeCreated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 修正后的时间格式，匹配预期格式
    val timeCreated: LocalDateTime? = null
)
