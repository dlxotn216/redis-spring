package io.taesu.redisspring.limiter.interfaces

import io.taesu.redisspring.limiter.application.MyRankRetrieveService
import io.taesu.redisspring.limiter.domain.UserLimitedJob
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@RestController
class MyRankRetrieveController(private val myRankRetrieveService: MyRankRetrieveService) {
    @GetMapping("/api/v1/limited-jobs/{jobId}/users/{userKey}/ranks")
    fun rank(request: UserLimitedJob): MyRankRetrieveResponse {
        return myRankRetrieveService.rank(request)
    }
}

