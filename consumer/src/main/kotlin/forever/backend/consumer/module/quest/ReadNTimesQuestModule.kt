package forever.backend.consumer.module.quest

import forever.backend.base.enums.QuestType
import forever.backend.mysql.entity.content.Quest
import forever.backend.mysql.entity.content.Reward
import org.springframework.stereotype.Component

@Component
class ReadNTimesQuestModule : QuestModule {
    override val questType = QuestType.READ_N_TIMES

    override fun process(userId: Long, quest: Quest): List<Reward> {
        TODO("누적으로 어떻게 처리할지 고민해보자.")
    }
}
