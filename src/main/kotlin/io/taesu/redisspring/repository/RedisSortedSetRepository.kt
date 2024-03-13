package io.taesu.redisspring.repository

import org.springframework.data.redis.connection.RedisZSetCommands
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
        // return redisStringTemplate.execute {
        //     it.zSetCommands().zAdd(
        //         key.toByteArray(),
        //         Instant.now().epochSecond.toDouble(),
        //         value.toByteArray(),
        //         RedisZSetCommands.ZAddArgs.empty().gt()
        //     )
        // } ?: false
        return zSetOperations().add(key, value, Instant.now().epochSecond.toDouble()) ?: false
    }

    // 테스트 용으로만 사용할 것
    fun getAll(key: String): Set<String> {
        return zSetOperations().range(key, 0L, -1L) ?: emptySet()
    }

    fun popMax(key: String): ZSetOperations.TypedTuple<String>? {
        return zSetOperations().popMax(key)
    }

    fun popMax(key: String, count: Long): Set<ZSetOperations.TypedTuple<String>> {
        return zSetOperations().popMax(key, count) ?: emptySet()
    }

    fun random(key: String): String? {
        return zSetOperations().randomMember(key)
    }

    fun remove(key: String, value: String): Long? {
        return zSetOperations().remove(key, value)
    }

    fun size(key: String): Long {
        return zSetOperations().size(key) ?: 0L
    }

    fun rank(key: String, value: String, zeroBased: Boolean = false): Long? {
        val rank = zSetOperations().rank(key, value) ?: return null
        return rank.run {
            if (zeroBased) {
                this
            } else {
                this + 1
            }
        }
    }

    private fun zSetOperations() = redisStringTemplate.opsForZSet()
}
