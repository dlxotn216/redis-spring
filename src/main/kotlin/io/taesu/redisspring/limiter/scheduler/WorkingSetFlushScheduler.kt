package io.taesu.redisspring.limiter.scheduler

import io.taesu.redisspring.limiter.application.WorkingSetFlushService
import io.taesu.redisspring.limiter.domain.LimitedJob
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Component
class WorkingSetFlushScheduler(
    private val workingSetFlushService: WorkingSetFlushService
) {
    @Scheduled(fixedDelay = 2000, timeUnit = TimeUnit.MILLISECONDS)
    fun flushScheduler() {
        LimitedJob
            .entries
            .forEach {
                workingSetFlushService.flush(it)?.let {
                    log.info("flush :${it}")
                }
            }
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}
