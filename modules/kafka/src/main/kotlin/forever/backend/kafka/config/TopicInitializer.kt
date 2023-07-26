package forever.backend.kafka.config

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.errors.TopicExistsException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ExecutionException

@ConfigurationProperties(prefix = "spring.kafka")
data class KafkaTopicProperties(
    val topics: Map<String, TopicProperties>
) {
    data class TopicProperties(
        val name: String,
        val partitions: Int,
        val replicationFactor: Short,
    )
}

@Component
@ConfigurationPropertiesScan
class KafkaTopicCreator(
    private val kafkaTopicProperties: KafkaTopicProperties,
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String
) : SmartLifecycle {
    companion object {
        private val logger = LoggerFactory.getLogger(KafkaTopicCreator::class.java)
    }

    private var isRunning = false

    override fun start() {
        kafkaTopicProperties.topics.forEach { (topicName, topicProperties) ->
            val adminProps = Properties().apply {
                put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            }

            val newTopic = NewTopic(topicProperties.name, topicProperties.partitions, topicProperties.replicationFactor)

            runCatching {
                val adminClient = AdminClient.create(adminProps)
                val get = adminClient.createTopics(Collections.singleton(newTopic)).all().get()
                logger.info("Topic created successfully.")
                adminClient.close()
            }.onFailure {
                when (it) {
                    is ExecutionException -> {
                        if (it.cause is TopicExistsException) {
                            logger.info("Topic is already exists.")
                        } else {
                            logger.error("Topic creation failed.", it.cause)
                        }
                    }

                    else -> {
                        logger.error("Topic creation failed.", it)
                    }
                }
            }
        }

        isRunning = true
    }

    override fun stop() {
        // 애플리케이션 정지 시 실행할 로직
        isRunning = false
    }

    override fun isRunning(): Boolean {
        return isRunning
    }

    override fun getPhase(): Int {
        // 가장 먼저 실행하도록 가장 작은 숫자 반환
        return Integer.MIN_VALUE
    }
}

