package io.taesu.redisspring.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class RedisSortedSetRepository(private val redisStringTemplate: StringRedisTemplate) {
    fun add(key: String, value: String): Boolean {
        return redisStringTemplate.opsForZSet().add(key, value, Instant.now().epochSecond.toDouble()) ?: false
    }

    fun popMax(key: String): ZSetOperations.TypedTuple<String>? {
        return redisStringTemplate.opsForZSet().popMax(key)
    }

    fun rank(key: String, value: String): Long? {
        return redisStringTemplate.opsForZSet().rank(key, value)
    }
}
