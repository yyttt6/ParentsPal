package com.example.main.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.ElasticsearchTransport
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ElasticSearchConfig {

    @Value("\${es.hostname}")
    private lateinit var hostname: String

    @Value("\${es.port}")
    private var port: Int = 0

    @Bean
    open fun elasticSearchClient(): ElasticsearchClient {
        // 创建并配置 ObjectMapper
        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule()) // 注册 Java 8 日期/时间模块
        }

        // 创建使用配置的 ObjectMapper 的自定义 JacksonJsonpMapper
        val jacksonJsonpMapper = JacksonJsonpMapper(objectMapper)

        // 构建 RestClient 和 Transport
        val client: RestClient = RestClient.builder(HttpHost(hostname, port, "http")).build()
        val transport: ElasticsearchTransport = RestClientTransport(client, jacksonJsonpMapper)

        // 返回 ElasticsearchClient
        return ElasticsearchClient(transport)
    }
}
