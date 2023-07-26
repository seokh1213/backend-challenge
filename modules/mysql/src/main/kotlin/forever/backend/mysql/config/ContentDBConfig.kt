package forever.backend.mysql.config

import com.zaxxer.hikari.HikariDataSource
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "contentEntityManagerFactory",
    transactionManagerRef = "contentTransactionManager",
    basePackages = ["forever.backend.mysql.repository.content"]
)
class ContentDBConfig {

    @Bean("contentDataSource")
    fun contentDatasource(
        @Value("\${spring.datasource.content.driver-class-name}") driverClassName: String?,
        @Value("\${spring.datasource.content.url}") dataSourceUrl: String?,
        @Value("\${spring.datasource.content.username}") username: String?,
        @Value("\${spring.datasource.content.password}") password: String?
    ): DataSource? {
        val dataSource = DataSourceBuilder.create().type(HikariDataSource::class.java)
            .driverClassName(driverClassName)
            .url(dataSourceUrl)
            .username(username)
            .password(password)
            .build()
        dataSource.poolName = "content-pool"
        dataSource.connectionInitSql = "SET NAMES utf8mb4"
        return dataSource
    }

    private fun jpaProperties(): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy::class.java.name)
            put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy::class.java.name)
        }
    }

    @Bean("contentEntityManagerFactory")
    fun contentEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier("contentDataSource") contentDataSource: DataSource?
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(contentDataSource)
            .packages("forever.backend.mysql.entity.content")
            .persistenceUnit("contents")
            .properties(jpaProperties())
            .build()
    }

    @Bean("contentTransactionManager")
    fun contentTransactionManager(
        @Qualifier("contentEntityManagerFactory") contentEntityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): JpaTransactionManager {
        return JpaTransactionManager(contentEntityManagerFactory.`object`!!)
    }

}
