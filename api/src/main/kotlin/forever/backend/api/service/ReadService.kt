package forever.backend.api.service

import forever.backend.kafka.KafkaSendService
import forever.backend.kafka.model.ReadActionMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ReadService(
    private val kafkaSendService: KafkaSendService,
    @Value("\${spring.kafka.topics.read.name}") private val readTopic: String
) {

    fun sendReadAction(articleId: Long, userId: Long) {
        kafkaSendService.send(readTopic, userId.toString(), ReadActionMessage(userId, articleId))
    }

}
