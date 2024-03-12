package io.taesu.redisspring.limiter.interfaces

import io.taesu.redisspring.limiter.interfaces.MyRankStatus.*

/**
 * Created by taesu on 2024/03/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
class MyRankRetrieveResponse(
    val status: MyRankStatus,
    val rank: Long? = null,
    val statusMessage: String
) {
    companion object {
        fun waiting(rank: Long) = MyRankRetrieveResponse(
            status = WAITING,
            rank = rank,
            statusMessage = "현재 대기 순번은 [$rank]번째 입니다."
        )

        fun working() = MyRankRetrieveResponse(
            status = WORKING,
            statusMessage = "이미 작업 중입니다."
        )

        fun finished() = MyRankRetrieveResponse(
            status = FINISHED,
            statusMessage =             """
            작업을 이미 완료하였거나 정상 대기열 요청이 접수되지 않았습니다.
            재접속 해주세요.
        """.trimIndent()

        )
    }
}

enum class MyRankStatus {
    WAITING,
    WORKING,
    FINISHED
}
