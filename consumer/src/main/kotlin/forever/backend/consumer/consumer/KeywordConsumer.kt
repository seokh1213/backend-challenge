package forever.backend.consumer.consumer

import forever.backend.consumer.service.KeywordService
import forever.backend.kafka.model.ArticleMessage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KeywordConsumer(
    private val keywordService: KeywordService
) {


    @KafkaListener(topics = ["\${spring.kafka.topics.article.name}"])
    fun listener(articleMessage: ArticleMessage) {
        keywordService.process(articleMessage)
    }

}
