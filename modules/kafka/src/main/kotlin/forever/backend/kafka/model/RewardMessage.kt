package forever.backend.kafka.model

import forever.backend.base.enums.RewardType

data class RewardMessage(
    val userId: Long,
    val rewardType: RewardType
)
