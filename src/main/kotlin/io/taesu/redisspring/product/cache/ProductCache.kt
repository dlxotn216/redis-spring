package io.taesu.redisspring.product.cache

/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
class ProductCache(
    val productKey: Long,
    val name: String,
    val imageUrl: String,
)
