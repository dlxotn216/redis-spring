package io.taesu.redisspring.product.infra

/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
interface ProductSearchAdapter {
    fun search(keyword: String): List<Long> {
        return (1L..100L).toList()
    }
}
