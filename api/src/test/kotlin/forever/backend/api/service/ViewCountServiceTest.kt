package forever.backend.api.service

import forever.backend.mysql.entity.content.ViewCount
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ViewCountServiceTest {

    @Autowired
    private lateinit var viewCountService: ViewCountService

    @Test
    @DisplayName("동기로 증가시 정확히 맞아야한다.")
    fun viewCountBlock() {
        val viewCount = viewCountService.saveViewCount(ViewCount(viewCount = 0))
        println("viewCount: $viewCount")

        Assertions.assertThat(viewCount.viewCount).isEqualTo(0)

        val rangeEnd = 5
        (1..rangeEnd).forEach { _ ->
            viewCountService.increaseViewCount(viewCountId = viewCount.id!!)
        }

        val existedViewCount = viewCountService.findByViewCountId(viewCountId = viewCount.id!!)!!
        Assertions.assertThat(existedViewCount.viewCount).isEqualTo(rangeEnd)

        println("existedViewCount: $existedViewCount")
    }

    @Test
    @DisplayName("비동기로 단순 조회 후 증가시 누락된다.")
    fun viewCountMultiThreadWithNaive() {
        val viewCount = viewCountService.saveViewCount(ViewCount(viewCount = 0))
        println("viewCount: $viewCount")

        Assertions.assertThat(viewCount.viewCount).isEqualTo(0)

        val rangeEnd = 5
        (1..rangeEnd).toList().parallelStream().forEach { _ ->
            viewCountService.increaseViewCount(viewCountId = viewCount.id!!)
        }

        val existedViewCount = viewCountService.findByViewCountId(viewCountId = viewCount.id!!)!!
        Assertions.assertThat(existedViewCount.viewCount).isNotEqualTo(rangeEnd)

        println("existedViewCount: $existedViewCount")
    }

    @Test
    @DisplayName("비동기로 @Transactional(Serializable)을 사용해도 누락된다.")
    fun viewCountMultiThreadWithSerializable() {
        val viewCount = viewCountService.saveViewCount(ViewCount(viewCount = 0))
        println("viewCount: $viewCount")

        Assertions.assertThat(viewCount.viewCount).isEqualTo(0)

        val rangeEnd = 5
        (1..rangeEnd).toList().parallelStream().forEach { _ ->
            runCatching {
                viewCountService.increaseViewCountWithTransactionalSerializable(viewCountId = viewCount.id!!)
            }
        }

        val existedViewCount = viewCountService.findByViewCountId(viewCountId = viewCount.id!!)!!
        Assertions.assertThat(existedViewCount.viewCount).isNotEqualTo(rangeEnd)

        println("existedViewCount: $existedViewCount")
    }

    @Test
    @DisplayName("비동기로 @Lock을 사용하면 정확히 맞는다.")
    fun viewCountMultiThreadWithLock() {
        val viewCount = viewCountService.saveViewCount(ViewCount(viewCount = 0))
        println("viewCount: $viewCount")

        Assertions.assertThat(viewCount.viewCount).isEqualTo(0)

        val rangeEnd = 5
        (1..rangeEnd).toList().parallelStream().forEach { _ ->
            viewCountService.increaseViewCountWithLock(viewCountId = viewCount.id!!)
        }

        val existedViewCount = viewCountService.findByViewCountId(viewCountId = viewCount.id!!)!!
        Assertions.assertThat(existedViewCount.viewCount).isEqualTo(rangeEnd)

        println("existedViewCount: $existedViewCount")
    }

}
