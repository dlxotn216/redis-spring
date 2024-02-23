package io.taesu.redisspring.product.infra.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.taesu.redisspring.peralgorithm.StringRedisRepository
import io.taesu.redisspring.product.cache.ProductCache
import io.taesu.redisspring.product.infra.ProductCacheAdapter
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.stereotype.Component
import java.time.Duration


/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class ProductRedisCacheAdapter(
    private val stringRedisRepository: StringRedisRepository,
    private val objectMapper: ObjectMapper
): ProductCacheAdapter {

    @PostConstruct
    fun init() {
        (1L..100L).map {
            stringRedisRepository.save(
                "product:$it",
                Duration.ofDays(1L)
            ) {
                objectMapper.writeValueAsString(
                    ProductCache(
                        it,
                        "product $it",
                        "https://image.service.io/products/$it"
                    )
                )
            }
        }
    }

    override fun findAll(productKeys: List<Long>): List<ProductCache> {
        // return productKeys.mapNotNull {
        //     objectMapper.readValue(stringRedisRepository.simpleGet("product:$it"), ProductCache::class.java)
        // }

        // return stringRedisRepository.simpleMGet(productKeys.map { "product:$it" })
        //     .map {
        //         objectMapper.readValue(it, ProductCache::class.java)
        //     }

        // return stringRedisRepository.simplePipelineGet(productKeys.map { "product:$it" })
        //     .map {
        //         objectMapper.readValue(it, ProductCache::class.java)
        //     }

        return stringRedisRepository.simpleLuaGet(productKeys.map { "product:$it" })
            .map {
                objectMapper.readValue(it, ProductCache::class.java)
            }
    }
}
