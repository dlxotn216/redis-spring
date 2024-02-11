package io.taesu.redisspring.usecase

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024/02/11.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class RedisString(private val redisStringTemplate: StringRedisTemplate) {
    fun test() {
        redisStringTemplate.opsForValue().set("hello", "world")
        assert(redisStringTemplate.opsForValue().get("hello") == "world")

        // set NX
        redisStringTemplate.opsForValue().setIfPresent("hello", "world2")
        assert(redisStringTemplate.opsForValue().get("hello") == "world2")

        // set XX
        redisStringTemplate.opsForValue().setIfAbsent("hello", "world3")
        assert(redisStringTemplate.opsForValue().get("hello") != "world3")

        // getset
        assert(redisStringTemplate.opsForValue().getAndSet("hello", "world3") == "world2")
        assert(redisStringTemplate.opsForValue().get("hello") == "world3")

        redisStringTemplate.delete("counter")
        // incr
        redisStringTemplate.opsForValue().increment("counter")
        assert(redisStringTemplate.opsForValue().get("counter") == "1")

        redisStringTemplate.opsForValue().increment("counter", 10)
        assert(redisStringTemplate.opsForValue().get("counter") == "11")

        redisStringTemplate.opsForValue().multiSet(mapOf(
            "name" to "Lee Tae Su",
            "age" to "30"
        ))
        redisStringTemplate.opsForValue().multiGet(listOf("name", "age"))!!.also {
            assert(it[0] == "Lee Tae Su")
            assert(it[1] == "30")
        }
    }
}
