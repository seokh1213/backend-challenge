package forever.backend.mysql.repository.content

import forever.backend.mysql.entity.content.ViewCount
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ViewCountRepository : JpaRepository<ViewCount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vc FROM ViewCount vc WHERE vc.id = :id")
    fun findByIdWithLock(id: Long): ViewCount?

}
