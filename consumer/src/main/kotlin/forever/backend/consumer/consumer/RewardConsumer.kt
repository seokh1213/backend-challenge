package forever.backend.consumer.consumer

import forever.backend.consumer.service.RewardService
import forever.backend.kafka.model.RewardMessage
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class RewardConsumer(
    private val rewardService: RewardService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RewardConsumer::class.java)
    }

    @KafkaListener(topics = ["\${spring.kafka.topics.reward.name}"], groupId = "keyword")
    fun listener(rewardMessage: RewardMessage) {
        logger.info("[consumer] read action consumed. {}", rewardMessage)
        rewardService.process(rewardMessage.userId, rewardMessage.rewardType)
    }

}
