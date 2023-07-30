package forever.backend.mysql.repository.content

import forever.backend.mysql.entity.content.TargetGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TargetGroupRepository : JpaRepository<TargetGroup, Long> {
    fun findAllByTargetGroupIdIn(targetGroupIdList: List<Long>): List<TargetGroup>
}
