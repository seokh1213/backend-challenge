package forever.backend.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@EnableJpaAuditing
@EnableConfigurationProperties
@SpringBootApplication(scanBasePackages = ["forever.backend"])
class BackendChallengeApplication

fun main(args: Array<String>) {
    runApplication<BackendChallengeApplication>(*args)
}
