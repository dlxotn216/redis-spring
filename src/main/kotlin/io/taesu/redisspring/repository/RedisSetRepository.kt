package io.taesu.redisspring.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class RedisSetRepository(private val redisStringTemplate: StringRedisTemplate) {
    fun isMember(key: String, value: String): Boolean {
        return setOperations().isMember(key, value) ?: false
    }

    fun size(key: String): Long {
        return setOperations().size(key) ?: 0L
    }

    fun add(key: String, value: String): Long? {
        return setOperations().add(key, value)
    }

    fun remove(key: String, value: String): Long? {
        return setOperations().remove(key, value)
    }

    private fun setOperations() = redisStringTemplate.opsForSet()
}
