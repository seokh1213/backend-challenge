package forever.backend.api.service

import forever.backend.kafka.model.ReadActionMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ReadService(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.topics.read.name}") private val readTopic: String
) {

    fun sendReadAction(articleId: Long, userId: Long) {
        kafkaTemplate.send(readTopic, userId.toString(), ReadActionMessage(userId, articleId))
    }

}
