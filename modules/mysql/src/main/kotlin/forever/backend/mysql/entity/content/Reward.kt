package forever.backend.mysql.entity.content

import forever.backend.base.enums.RewardType
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
data class Reward(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var rewardGroupId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var rewardType: RewardType,

    @CreatedDate
    var createdDt: Instant? = null,
    @LastModifiedDate
    var updatedDt: Instant? = null
)
