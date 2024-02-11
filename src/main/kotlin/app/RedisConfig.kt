package app

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


/**
 * Created by itaesu on 2024/02/09.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Configuration
class RedisConfig {
    @Bean
    fun redisConnectionFactory(redisInfo: RedisInfo): RedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration(
            redisInfo.host,
            redisInfo.port
        ).apply {
            this.setPassword(redisInfo.password)
        }

        val clientConfig = LettuceClientConfiguration.builder()
            .clientName("client")
            .commandTimeout(Duration.ofSeconds(1))
            .build()
        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

    @Bean
    fun redisTemplate(redisInfo: RedisInfo): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            this.keySerializer = RedisSerializer.string()
            this.valueSerializer = RedisSerializer.string()

            this.hashKeySerializer = RedisSerializer.string()
            this.hashValueSerializer = RedisSerializer.string()

            this.setDefaultSerializer(RedisSerializer.string())
            this.connectionFactory = redisConnectionFactory(redisInfo)
        }
    }

    @Bean
    fun redisStringTemplate(redisInfo: RedisInfo): StringRedisTemplate {
        // RedisTemplate<String, String>, 모든 serializer가 StringRedisSerializer
        return StringRedisTemplate(redisConnectionFactory(redisInfo))
    }
}

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisInfo @ConstructorBinding constructor(
    val host: String,
    val port: Int,
    val password: String,
    val clientName: String,
)
