package forever.backend.mysql.repository.content

import forever.backend.mysql.entity.content.Target
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TargetRepository : JpaRepository<Target, Long> {

    fun findAllByUserId(userId: Long): List<Target>

}
