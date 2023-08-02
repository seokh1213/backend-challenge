package forever.backend.consumer.module.quest

import forever.backend.base.enums.QuestType
import forever.backend.mysql.entity.content.Quest
import forever.backend.mysql.entity.content.Reward

interface QuestModule {
    val questType: QuestType

    fun process(userId: Long, quest: Quest): List<Reward>
}
