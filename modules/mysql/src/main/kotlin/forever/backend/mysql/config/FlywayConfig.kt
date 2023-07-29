package forever.backend.mysql.config

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig {

    @Bean
    fun flywayContent(@Qualifier("contentDataSource") contentDataSource: DataSource): CommandLineRunner {

        return CommandLineRunner {
            val config = FluentConfiguration()
                .dataSource(contentDataSource)
                .locations("db/migration/content")
                .baselineVersion("0")

            val flyway = Flyway.configure().configuration(config).load()
            flyway.baseline()
            flyway.migrate()
        }
    }

}
