package io.taesu.redisspring.limiter.application

import io.taesu.redisspring.limiter.domain.LimitedJob
import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.limiter.interfaces.WorkingSetIsFullResponse
import io.taesu.redisspring.peralgorithm.StringRedisRepository
import io.taesu.redisspring.repository.RedisSortedSetRepository
import org.springframework.stereotype.Service

@Service
class WorkingSetRetrieveService(
    private val redisSortedSetRepository: RedisSortedSetRepository,
    private val redisStringRepository: StringRedisRepository,
) {
    fun retrieveAll(jobId: LimitedJob): Set<String> {
        return redisSortedSetRepository.getAll(jobId.workingSetName)
    }

    fun tryToAcquire(userLimitedJob: UserLimitedJob): Boolean {
        return with(userLimitedJob) {
            // FIXME atomic 하도록 lua에서 처리
            if (retrieveStatistic(userLimitedJob.jobId).full) {
                false
            } else {
                redisSortedSetRepository.add(this.jobId.workingSetName, this.userKey.toString())
            }
        }
    }

    fun exists(userLimitedJob: UserLimitedJob): Boolean {
        return with(userLimitedJob) {
            redisSortedSetRepository.rank(jobId.workingSetName, userKey.toString()) != null
        }
    }

    fun retrieveStatistic(jobId: LimitedJob): WorkingSetIsFullResponse {
        val maxWorkingSetSize = redisStringRepository.simpleGet(jobId.workingSetMaxSizeName)?.toLongOrNull() ?: 0L
        val workingSetSize = redisSortedSetRepository.size(jobId.workingSetName)
        return WorkingSetIsFullResponse(maxWorkingSetSize, workingSetSize)
    }

    fun size(jobId: LimitedJob): Long {
        return redisSortedSetRepository.size(jobId.workingSetName)
    }
}
