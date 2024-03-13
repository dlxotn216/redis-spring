package io.taesu.redisspring.limiter.domain

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
enum class LimitedJob(
    val queueName: String,
    val workingSetName: String,
    val workingSetMaxSizeName: String,
) {
    SAVE_REPORT(
        "job:queue:save_report",           // 대기열의 키 이름
        "job:workingset:savereport",       // 워킹셋의 키 이름
        "job:workingsetmaxsize:savereport",       // 워킹셋의 작업 최대 크기 키 이름
    )
}
