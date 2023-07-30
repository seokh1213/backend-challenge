package forever.backend.consumer.module.reward

import forever.backend.base.enums.RewardType

abstract class RewardModule {
    abstract fun rewardType(): RewardType

    abstract fun process(userId: Long)
}
