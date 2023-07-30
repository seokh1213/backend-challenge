package forever.backend.consumer.module.quest

import forever.backend.base.enums.QuestType
import forever.backend.mysql.entity.content.Quest
import forever.backend.mysql.entity.content.Reward

abstract class QuestModule {
    abstract fun questType(): QuestType

    abstract fun process(userId: Long, quest: Quest): List<Reward>
}
