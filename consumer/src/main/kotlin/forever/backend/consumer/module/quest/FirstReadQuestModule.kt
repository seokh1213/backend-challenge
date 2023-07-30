package forever.backend.consumer.module.quest

import forever.backend.base.enums.QuestType
import forever.backend.mysql.entity.content.Quest
import forever.backend.mysql.entity.content.Reward
import forever.backend.mysql.repository.content.RewardRepository
import org.springframework.stereotype.Component

@Component
class FirstReadQuestModule(
    private val rewardRepository: RewardRepository
) : QuestModule() {

    override fun questType() = QuestType.FIRST_READ

    override fun process(userId: Long, quest: Quest): List<Reward> {
        if (!validate(userId, quest)) {
            return emptyList()
        }

        return rewardRepository.findAllByRewardGroupId(quest.rewardGroupId)
    }

    private fun validate(userId: Long, quest: Quest) = true // TODO
}
