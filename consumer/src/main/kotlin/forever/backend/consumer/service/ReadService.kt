package forever.backend.consumer.service

import forever.backend.consumer.module.quest.QuestModule
import forever.backend.kafka.model.ReadActionMessage
import forever.backend.kafka.model.RewardMessage
import forever.backend.mysql.entity.content.Quest
import forever.backend.mysql.repository.content.QuestRepository
import forever.backend.mysql.repository.content.TargetGroupRepository
import forever.backend.mysql.repository.content.TargetRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ReadService(
    private val targetRepository: TargetRepository,
    private val targetGroupRepository: TargetGroupRepository,
    private val questRepository: QuestRepository,
    @Qualifier("roundRobinKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    @Value("\${spring.kafka.topics.reward.name}") private val rewardTopic: String,
    questModuleList: List<QuestModule>
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ReadService::class.java)
    }

    private val questModuleMap = questModuleList.associateBy { it.questType() }

    fun checkReadQuest(readActionMessage: ReadActionMessage) {
        val activeQuestList = getActiveQuestList(readActionMessage.userId)

        activeQuestList.asSequence()
            .mapNotNull { quest ->
                questModuleMap[quest.questType]?.let { it to quest }
            }.flatMap { (module, quest) ->
                module.process(readActionMessage.userId, quest)
            }.forEach {
                kafkaTemplate.send(rewardTopic, RewardMessage(readActionMessage.userId, it.rewardType)) // reward 지급
            }
    }

    private fun getActiveQuestList(userId: Long): List<Quest> {
        val targetList = targetRepository.findAllByUserId(userId)
            .takeIf { it.isNotEmpty() } ?: return emptyList()
        val targetGroupList = targetGroupRepository.findAllByTargetGroupIdIn(targetList.map { it.targetGroupId })
            .takeIf { it.isNotEmpty() } ?: return emptyList()
        return questRepository.findAllByIdInAndActivation(targetGroupList.map { it.questId }, true)
    }

}
