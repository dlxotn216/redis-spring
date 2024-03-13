package io.taesu.redisspring.limiter.application

import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.repository.RedisSortedSetRepository
import org.springframework.stereotype.Service

@Service
class JobEnqueueService(
    private val redisSortedSetRepository: RedisSortedSetRepository,
) {
    fun enqueue(userLimitedJob: UserLimitedJob): Boolean {
        return with(userLimitedJob) {
            redisSortedSetRepository.add(jobId.queueName, userKey.toString())
        }
    }
}
