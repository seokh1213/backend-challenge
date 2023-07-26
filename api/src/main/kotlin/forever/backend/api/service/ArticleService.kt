package forever.backend.api.service

import forever.backend.api.model.request.CreateArticleRequest
import forever.backend.kafka.model.ArticleMessage
import forever.backend.mysql.entity.content.Article
import forever.backend.mysql.repository.content.ArticleRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    @Lazy private val self: ArticleService,
    private val articleRepository: ArticleRepository,
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.topics.article.name}") private val articleTopic: String,
) {

    fun createArticle(createArticleRequest: CreateArticleRequest): Article {
        val article = articleRepository.save(
            Article(
                title = createArticleRequest.title,
                content = createArticleRequest.content
            )
        )

        // kafka event 발행
        sendArticleMessage(articleTopic, ArticleMessage(article.id!!, article.title, article.content))

        return article
    }

    fun sendArticleMessage(topic: String, message: ArticleMessage) {
        kafkaTemplate.send(topic, message.id.toString(), message)
    }

    @Transactional
    fun test() {
        articleRepository.save(Article(title = "short title", content = "test"))
        val result = runCatching {
            self.test2()
        }

        println(result.exceptionOrNull())
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun test2() {
        val result = runCatching {
            val longTitle = "a".repeat(1001)
            articleRepository.save(Article(title = longTitle, content = "test")) // DataIntegrityViolationException 발생
        }
        println(result.isSuccess)
    }


}
