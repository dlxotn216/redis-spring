package io.taesu.redisspring.product.application

import io.taesu.redisspring.product.infra.ProductCacheAdapter
import io.taesu.redisspring.product.infra.ProductSearchAdapter
import org.springframework.stereotype.Service

/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Service
class ProductSearchService(
    private val productSearchAdapter: ProductSearchAdapter,
    private val productCacheAdapter: ProductCacheAdapter,
) {
    fun search(keyword: String): List<ProductSearchRow> {
        val productKeys = productSearchAdapter.search(keyword)
            .takeIf { it.isNotEmpty() } ?: return emptyList()

        return productCacheAdapter.findAll(productKeys)
            .map { ProductSearchRow(it.productKey, it.name, it.imageUrl) }
    }
}

class ProductSearchRow(
    val productKey: Long,
    val name: String,
    val imageUrl: String,
)
