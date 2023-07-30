package forever.backend.kafka.model

data class ReadActionMessage(
    val userId: Long,
    val articleId: Long
)
