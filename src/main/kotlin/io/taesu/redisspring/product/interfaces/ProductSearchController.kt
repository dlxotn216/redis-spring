package io.taesu.redisspring.product.interfaces

import io.taesu.redisspring.product.application.ProductSearchRow
import io.taesu.redisspring.product.application.ProductSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by taesu on 2024/02/24.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@RestController
class ProductSearchController(private val productSearchService: ProductSearchService) {
    @GetMapping("/api/v1/products/search")
    fun search(keyword: String): List<ProductSearchRow> {
        return productSearchService.search(keyword)
    }
}
