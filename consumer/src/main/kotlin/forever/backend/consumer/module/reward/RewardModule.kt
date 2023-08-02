package forever.backend.consumer.module.reward

import forever.backend.base.enums.RewardType

interface RewardModule {
    val rewardType: RewardType

    fun process(userId: Long)
}
