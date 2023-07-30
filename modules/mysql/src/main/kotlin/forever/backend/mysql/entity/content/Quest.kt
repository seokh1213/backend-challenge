package forever.backend.mysql.entity.content

import forever.backend.base.enums.QuestType
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class Quest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var rewardGroupId: Long,

    @Column(nullable = false)
    var activation: Boolean,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var questType: QuestType,

    @CreatedDate
    var createdDt: Instant? = null,
    @LastModifiedDate
    var updatedDt: Instant? = null
)
