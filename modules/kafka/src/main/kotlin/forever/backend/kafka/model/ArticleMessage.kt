package forever.backend.kafka.model

data class ArticleMessage(
    val id: Long,
    val title: String,
    val content: String
)
