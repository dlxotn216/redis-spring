package io.taesu.redisspring.limiter.application

import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.limiter.interfaces.WorkingSetAcquireResponse
import org.springframework.stereotype.Service

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Service
class WorkingSetAcquireService(
    private val workingSetRetrieveService: WorkingSetRetrieveService,
    private val jobEnqueueService: JobEnqueueService,
    private val myRankRetrieveService: MyRankRetrieveService
) {
    fun acquire(userLimitedJob: UserLimitedJob): WorkingSetAcquireResponse {
        if (workingSetRetrieveService.tryToAcquire(userLimitedJob)) return WorkingSetAcquireResponse.success()
        if (workingSetRetrieveService.exists(userLimitedJob)) return WorkingSetAcquireResponse.alreadyAcquired()

        jobEnqueueService.enqueue(userLimitedJob)
        return WorkingSetAcquireResponse.enqueued(myRankRetrieveService.rank(userLimitedJob))
    }
}
