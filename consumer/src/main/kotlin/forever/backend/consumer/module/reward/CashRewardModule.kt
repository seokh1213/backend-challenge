package forever.backend.consumer.module.reward

import forever.backend.base.enums.RewardType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CashRewardModule : RewardModule() {
    companion object {
        private val logger = LoggerFactory.getLogger(CashRewardModule::class.java)
    }

    override fun rewardType() = RewardType.CASH

    override fun process(userId: Long) {
        logger.info("CashRewardModule process")
    }

}
