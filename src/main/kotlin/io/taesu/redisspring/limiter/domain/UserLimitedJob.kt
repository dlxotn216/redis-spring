package io.taesu.redisspring.limiter.domain

data class UserLimitedJob(
    val jobId: LimitedJob,
    val userKey: Long
)
