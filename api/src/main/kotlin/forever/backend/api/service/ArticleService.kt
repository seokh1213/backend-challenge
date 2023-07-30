package forever.backend.api.service

import forever.backend.api.model.request.CreateArticleRequest
import forever.backend.kafka.model.ArticleMessage
import forever.backend.mysql.entity.content.Article
import forever.backend.mysql.repository.content.ArticleRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.topics.article.name}") private val articleTopic: String
) {

    fun createArticle(createArticleRequest: CreateArticleRequest): Article {
        val article = articleRepository.save(
            Article(
                title = createArticleRequest.title,
                content = createArticleRequest.content
            )
        )

        // kafka event 발행
        kafkaTemplate.send(
            articleTopic,
            article.id!!.toString(),
            ArticleMessage(article.id!!, article.title, article.content)
        )

        return article
    }

}
