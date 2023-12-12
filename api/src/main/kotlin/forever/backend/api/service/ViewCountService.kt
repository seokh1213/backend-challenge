package forever.backend.api.service

import forever.backend.mysql.entity.content.ViewCount
import forever.backend.mysql.repository.content.ViewCountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ViewCountService(
    private val viewCountRepository: ViewCountRepository
) {

    fun findByViewCountId(viewCountId: Long) = viewCountRepository.findById(viewCountId).getOrNull()

    fun saveViewCount(viewCount: ViewCount) = viewCountRepository.save(viewCount)

    fun increaseViewCount(viewCountId: Long) {
        val viewCount = viewCountRepository.findById(viewCountId).getOrNull() ?: return

        viewCount.viewCount += 1

        viewCountRepository.save(viewCount)
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun increaseViewCountWithTransactionalSerializable(viewCountId: Long) {
        val viewCount = viewCountRepository.findById(viewCountId).getOrNull() ?: return

        viewCount.viewCount += 1

        viewCountRepository.save(viewCount)
    }

    @Transactional
    fun increaseViewCountWithLock(viewCountId: Long) {
        val viewCount = viewCountRepository.findByIdWithLock(viewCountId) ?: return

        viewCount.viewCount += 1

        viewCountRepository.save(viewCount)
    }

}
