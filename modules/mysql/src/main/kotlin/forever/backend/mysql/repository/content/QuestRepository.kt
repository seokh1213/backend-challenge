package forever.backend.mysql.repository.content

import forever.backend.mysql.entity.content.Quest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestRepository : JpaRepository<Quest, Long> {

    fun findAllByIdInAndActivation(idList: List<Long>, activation: Boolean): List<Quest>

}
