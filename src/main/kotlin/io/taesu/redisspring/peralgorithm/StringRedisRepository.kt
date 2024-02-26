package io.taesu.redisspring.peralgorithm

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import kotlin.math.log10
import kotlin.random.Random


/**
 * Created by itaesu on 2024/02/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class StringRedisRepository(private val redisStringTemplate: StringRedisTemplate) {
    fun simpleGet(key: String): String? {
        return redisStringTemplate.opsForValue().get(key)
    }

    fun simpleMGet(keys: List<String>): List<String> {
        return redisStringTemplate.opsForValue().multiGet(keys) ?: emptyList()
    }

    fun simplePipelineGet(keys: List<String>): List<String> {
        return redisStringTemplate.executePipelined { conn ->
            keys.forEach {
                conn.stringCommands().get(redisStringTemplate.stringSerializer.serialize(it)!!)
            }
            null // return null
        }.map { it.toString() }
    }

    fun simpleLuaGet(keys: List<String>): List<String> {
        val script = RedisScript<List<String>>(
            """
            local results = {}
            for i, key in ipairs(KEYS) do
                results[i] = redis.call('GET', key) 
            end
            return results
        """.trimIndent()
        )
        return redisStringTemplate.execute(script, keys)
    }

    fun get(key: String, ttl: Duration, beta: Double = 1.0, recompute: () -> String): String? {
        val nowTime = Instant.now().toEpochMilli()
        val (value, delta, expiry) = get(key, nowTime) ?: return null

        val perGap = delta * beta * log10(Random.nextDouble(0.0, 1.0))
        log.info("nowTime: $nowTime, perGap: $perGap, expiry: $expiry")
        if ((nowTime - perGap) >= expiry) {
            log.info("Recompute")
            val recomputedValue = recompute()
            val newDelta = Instant.now().toEpochMilli() - nowTime
            save(key, recomputedValue, newDelta, ttl)
        }
        return value
    }

    private fun get(key: String, nowTime: Long): RedisGetEntry? {
        val script = RedisScript<List<String?>>(
            """
                return 
                {
                    redis.call('GET', KEYS[1]),
                    redis.call('GET', KEYS[2]),
                    redis.call('TTL', KEYS[1])
                }
            """.trimIndent()
        )
        return with(redisStringTemplate.execute(script, listOf(key, "$key:delta"))) {
            val value = this[0] ?: return null
            val delta = (this[1]?.toLong()) ?: return null
            val expire = (this[2] as? Long?) ?: return null
            val expiry = expire + nowTime
            RedisGetEntry(value, delta, expiry)
        }
    }

    fun save(key: String, ttl: Duration, compute: () -> String) {
        val nowTime = Instant.now().toEpochMilli()
        val value = compute()
        val delta = Instant.now().toEpochMilli() - nowTime
        save(key, value, delta, ttl)
    }

    fun save(key: String, value: String, delta: Long, ttl: Duration) {
        val script = RedisScript<Unit>(
        """
            redis.call('SET', KEYS[1], ARGV[1]);
            redis.call('SET', KEYS[2], ARGV[2]);
            redis.call('EXPIRE', KEYS[1], ARGV[3]);
            redis.call('EXPIRE', KEYS[2], ARGV[3]);
        """.trimIndent()
        )
        redisStringTemplate.execute(script, listOf(key, "$key:delta"), value, delta.toString(), ttl.seconds.toString())
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}

data class RedisGetEntry(
    val value: String,
    val delta: Long,
    val expiry: Long,
)
