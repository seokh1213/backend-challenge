package forever.backend.mysql.entity.content

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 1000)
    var name: String,

    @CreatedDate
    var createdDt: Instant? = null,
    @LastModifiedDate
    var updatedDt: Instant? = null
)
