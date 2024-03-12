package io.taesu.redisspring.limiter.domain

class UserLimitedJob(
    val jobId: LimitedJob,
    val userKey: Long
)
