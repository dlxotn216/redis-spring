package io.taesu.redisspring.limiter.interfaces

import io.taesu.redisspring.limiter.application.WorkingSetRetrieveService
import io.taesu.redisspring.limiter.domain.LimitedJob
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Created by taesu on 2024/03/13.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@RestController
class WorkingSetRetrieveController(
    private val workingSetRetrieveService: WorkingSetRetrieveService
) {
    @GetMapping("/api/v1/limited-jobs/{jobId}/working-set")
    fun retrieve(@PathVariable jobId: LimitedJob): Set<String> {
        return workingSetRetrieveService.retrieveAll(jobId)
    }
}
