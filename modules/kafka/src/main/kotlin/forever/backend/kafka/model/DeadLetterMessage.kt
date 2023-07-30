package forever.backend.kafka.model

data class DeadLetterMessage(
    val originalTopic: String,
    val originalValue: Any,
    val originalKey: String? = null,
    val exceptionMessage: String? = null
)
