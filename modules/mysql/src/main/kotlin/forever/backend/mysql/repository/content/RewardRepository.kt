package forever.backend.mysql.repository.content

import forever.backend.mysql.entity.content.Reward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardRepository : JpaRepository<Reward, Long> {
    fun findAllByRewardGroupId(rewardGroupId: Long): List<Reward>
}
