package forever.backend.mysql.entity.content

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class ViewCount(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var viewCount: Int,

    @CreatedDate
    var createdDt: Instant? = null,
    @LastModifiedDate
    var updatedDt: Instant? = null
)
