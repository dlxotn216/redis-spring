package io.taesu.redisspring.usecase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by itaesu on 2024/02/11.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@SpringBootTest
class RedisStringTest {
    @Autowired
    private lateinit var redisString: RedisString

    @Test
    fun `기본 string 자료구조 테스트`() {
        redisString.test()
    }

}
