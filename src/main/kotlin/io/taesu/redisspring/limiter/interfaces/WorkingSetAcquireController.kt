package io.taesu.redisspring.limiter.interfaces

import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.limiter.application.WorkingSetAcquireService
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
class WorkingSetAcquireController(
    private val workingSetAcquireService: WorkingSetAcquireService,
) {
    @GetMapping("/api/v1/limited-jobs/{jobId}/users/{userKey}")
    fun acquire(
        request: UserLimitedJob
    ): WorkingSetAcquireResponse {
        return workingSetAcquireService.acquire(request)
    }
}
