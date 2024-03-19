package io.taesu.redisspring.limiter.application

import io.taesu.redisspring.limiter.domain.LimitedJob
import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.repository.RedisSortedSetRepository
import org.springframework.stereotype.Service

/**
 * Created by taesu on 2024/03/13.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Service
class WorkingSetFlushService(
    private val redisSortedSetRepository: RedisSortedSetRepository
) {
    fun flush(jobId: LimitedJob): UserLimitedJob? {
        val random = redisSortedSetRepository.random(jobId.workingSetName) ?: return null
        return UserLimitedJob(jobId, random.toLong()).apply {
            delete(this)
        }
    }

    fun delete(userLimitedJob: UserLimitedJob) {
        userLimitedJob.run {
            redisSortedSetRepository.remove(this.jobId.workingSetName, userLimitedJob.userKey.toString())
        }
    }
}
