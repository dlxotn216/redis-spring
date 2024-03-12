package io.taesu.redisspring.limiter.application

import io.taesu.redisspring.limiter.domain.LimitedJob
import io.taesu.redisspring.limiter.domain.UserLimitedJob
import io.taesu.redisspring.limiter.interfaces.MyRankRetrieveResponse
import io.taesu.redisspring.repository.RedisSortedSetRepository
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service

@Service
class MyRankRetrieveService(
    private val redisSortedSetRepository: RedisSortedSetRepository,
    private val workingSetRetrieveService: WorkingSetRetrieveService,
) {
    fun rank(userLimitedJob: UserLimitedJob): MyRankRetrieveResponse {
        val rank = with(userLimitedJob) {
            redisSortedSetRepository.rank(jobId.queueName, userKey.toString())
                ?: return translateNotWorkingStatus(userLimitedJob)
        }

        return MyRankRetrieveResponse.waiting(rank)
    }

    private fun translateNotWorkingStatus(userLimitedJob: UserLimitedJob): MyRankRetrieveResponse {
        if (workingSetRetrieveService.exists(userLimitedJob)) {
            return MyRankRetrieveResponse.working()
        }
        return MyRankRetrieveResponse.finished()
    }

    fun pop(jobId: LimitedJob): ZSetOperations.TypedTuple<String>? {
        return redisSortedSetRepository.popMax(jobId.queueName)
    }
}
