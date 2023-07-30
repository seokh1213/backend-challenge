package forever.backend.consumer.service

import forever.backend.kafka.model.ArticleMessage
import forever.backend.redis.service.RedisService
import org.springframework.stereotype.Service

@Service
class KeywordService(
    private val redisService: RedisService
) {

    fun process(articleMessage: ArticleMessage) {
        runCatching {
            val keywordList: List<String> = redisService.getValue("keywordList") ?: emptyList()
            if (keywordList.isEmpty()) {
                return
            }

            val data = articleMessage.title + articleMessage.content

            val filteredKeywords = keywordList.parallelStream()
                .filter { data.contains(it) }
                .toList()
        }
    }

}
