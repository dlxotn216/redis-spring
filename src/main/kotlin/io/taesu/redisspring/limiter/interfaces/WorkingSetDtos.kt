package io.taesu.redisspring.limiter.interfaces

/**
 * Created by taesu on 2024/03/13.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */

class WorkingSetAcquireResponse(
    val status: AcquireStatus,
    val rank: MyRankRetrieveResponse? = null
) {
    enum class AcquireStatus {
        SUCCESS,
        ENQUEUED,
        FAIL_ALREADY_ACQUIRED,
    }

    companion object {
        fun success() = WorkingSetAcquireResponse(AcquireStatus.SUCCESS)
        fun enqueued(rank: MyRankRetrieveResponse) = WorkingSetAcquireResponse(AcquireStatus.ENQUEUED, rank)
        fun alreadyAcquired() = WorkingSetAcquireResponse(AcquireStatus.FAIL_ALREADY_ACQUIRED)
    }
}

class WorkingSetIsFullResponse(
    val maxWorkingSetSize: Long,
    val workingSetSize: Long,
) {
    val full get() = workingSetSize >= maxWorkingSetSize
    val diff get() = maxWorkingSetSize - workingSetSize
}
