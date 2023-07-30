package forever.backend.kafka

import forever.backend.kafka.model.DeadLetterMessage
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaSendService(
    private val kafkaTemplate: KafkaTemplate<String, Any>,

    @Qualifier("roundRobinKafkaTemplate")
    private val roundRobinKafkaTemplate: KafkaTemplate<String, Any>,

    @Value("\${spring.kafka.topics.dead-letter.name}")
    private val deadLetterTopic: String,
) {

    fun send(topic: String, key: String, value: Any) {
        kafkaTemplate.send(topic, key, value).exceptionallyAsync {
            if (it is UnknownTopicOrPartitionException) {
                // dead letter에 보내기
                kafkaTemplate.send(deadLetterTopic, DeadLetterMessage(topic, value, key, it.message))
            }

            throw it
        }
    }

    fun send(topic: String, value: Any) {
        roundRobinKafkaTemplate.send(topic, value).exceptionallyAsync {
            if (it is UnknownTopicOrPartitionException) {
                // dead letter에 보내기
                kafkaTemplate.send(deadLetterTopic, DeadLetterMessage(topic, value, exceptionMessage = it.message))
            }

            throw it
        }
    }

}
