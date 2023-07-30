package forever.backend.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.KafkaNull
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.util.backoff.FixedBackOff
import java.lang.reflect.Type

class CustomStringJsonMessageConverter(objectMapper: ObjectMapper) : StringJsonMessageConverter(objectMapper) {

    override fun extractAndConvertValue(record: ConsumerRecord<*, *>?, type: Type?): Any {
        if (record?.value() == null) {
            return KafkaNull.INSTANCE
        }

        if (type == String::class.java) {
            return record.value()
        }

        return super.extractAndConvertValue(record, type)
    }
}

@Configuration
class KafkaConsumerConfig {
    companion object {
        private val logger = LoggerFactory.getLogger(KafkaConsumerConfig::class.java)
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        recordMessageConverter: RecordMessageConverter
    ): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.setCommonErrorHandler(customErrorHandler())
        factory.setRecordMessageConverter(recordMessageConverter)
        return factory
    }

    @Bean
    fun recordMessageConverter(objectMapper: ObjectMapper): RecordMessageConverter {
        return CustomStringJsonMessageConverter(objectMapper)
    }

    private fun customErrorHandler(): DefaultErrorHandler {
        val errorHandler = DefaultErrorHandler(
            { consumerRecord, exception ->
                logger.error(
                    """
                        ${consumerRecord.topic()} consume Failure.
                   cause: ${exception.message}
                    ${consumerRecord.value()}
                """.trimIndent(), exception
                )
            },
            FixedBackOff(
                5000,
                3
            )
        )
        return errorHandler
    }

    @Bean
    fun consumerFactory(
        objectMapper: ObjectMapper,
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
        @Value("\${spring.kafka.consumer.group-id}") groupId: String,
        @Value("\${spring.kafka.consumer.auto-offset-reset}") autoOffsetReset: String,
        @Value("\${spring.kafka.consumer.max-poll-records}") maxPollRecords: String,
    ): ConsumerFactory<String, String> {
        val props = mutableMapOf<String, Any>().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
            put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName!!)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.qualifiedName!!)
        }

        return DefaultKafkaConsumerFactory(props)
    }

}

