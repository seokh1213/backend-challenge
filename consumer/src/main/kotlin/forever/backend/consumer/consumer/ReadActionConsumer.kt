package forever.backend.consumer.consumer

import forever.backend.consumer.service.ReadService
import forever.backend.kafka.model.ReadActionMessage
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ReadActionConsumer(
    private val readService: ReadService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ReadActionConsumer::class.java)
    }

    @KafkaListener(topics = ["\${spring.kafka.topics.read.name}"], groupId = "read-quest")
    fun listener(readActionMessage: ReadActionMessage) {
        logger.info("[consumer] read action consumed. {}", readActionMessage)
        readService.checkReadQuest(readActionMessage)
    }

}
