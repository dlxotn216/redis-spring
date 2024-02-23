package io.taesu.redisspring.peralgorithm

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit
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
        val value = redisStringTemplate.opsForValue().get(key) ?: return null
        val delta = redisStringTemplate.opsForValue().get("$key:delta")?.toLong() ?: return null
        val expire = redisStringTemplate.getExpire(key, TimeUnit.MILLISECONDS)
        val expiry = expire + nowTime
        return RedisGetEntry(value, delta, expiry)
    }

    fun save(key: String, ttl: Duration, compute: () -> String) {
        val nowTime = Instant.now().toEpochMilli()
        val value = compute()
        val delta = Instant.now().toEpochMilli() - nowTime
        save(key, value, delta, ttl)
    }

    fun save(key: String, value: String, delta: Long, ttl: Duration) {
        redisStringTemplate.opsForValue().set(key, value, ttl)
        redisStringTemplate.opsForValue().set("$key:delta", delta.toString(), ttl)
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
