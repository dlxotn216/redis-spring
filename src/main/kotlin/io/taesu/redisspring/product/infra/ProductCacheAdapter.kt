package io.taesu.redisspring.product.infra

import io.taesu.redisspring.product.cache.ProductCache

/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
interface ProductCacheAdapter {
    fun findAll(productKeys: List<Long>): List<ProductCache>
}
