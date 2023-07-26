package forever.backend.kafka.config

import com.fasterxml.jackson.databind.ObjectMapper
import forever.backend.kafka.partitioner.IntKeyPartitioner
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.record.CompressionType
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

    @Bean
    fun articleProducerFactory(
        objectMapper: ObjectMapper,
        @Value("\${spring.kafka.bootstrap-servers}") bootstrapServers: String,
        @Value("\${spring.kafka.producer.article.retries}") retries: String
    ): ProducerFactory<String, Any> {
        val configProps = mutableMapOf<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            put(ProducerConfig.RETRIES_CONFIG, retries)
            put(ProducerConfig.ACKS_CONFIG, "1")
            put(ProducerConfig.PARTITIONER_CLASS_CONFIG, IntKeyPartitioner::class.qualifiedName!!)
            put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY.name)
        }

        return DefaultKafkaProducerFactory(configProps, StringSerializer(), JsonSerializer(objectMapper))
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Any>): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory)
    }

}
