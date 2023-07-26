package forever.backend.mysql.entity.content

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 1000)
    var title: String,
    @Column(nullable = false, length = 10000)
    var content: String,

    @CreatedDate
    var createdDt: Instant? = null,
    @LastModifiedDate
    var updatedDt: Instant? = null
)
