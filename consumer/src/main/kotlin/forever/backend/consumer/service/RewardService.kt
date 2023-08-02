package forever.backend.consumer.service

import forever.backend.base.enums.RewardType
import forever.backend.consumer.module.reward.RewardModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RewardService(
    rewardModuleList: List<RewardModule>
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RewardService::class.java)
    }

    private val rewardModuleMap = rewardModuleList.associateBy { it.rewardType }

    fun process(userId: Long, rewardType: RewardType) {
        val rewardModule = rewardModuleMap[rewardType]
        if (rewardModule == null) {
            logger.warn("[{}] Reward Type is not supported.", rewardType)
            return
        }

        rewardModule.process(userId)
    }

}
