package io.taesu.redisspring.peralgorithm

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import kotlin.random.Random

/**
 * Created by itaesu on 2024/02/12.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@SpringBootTest
class StringRedisRepositoryTest {
    @Autowired
    private lateinit var stringRedisRepository: StringRedisRepository

    @Test
    fun `PER 알고리즘 테스트`() {
        val compute = {
            Thread.sleep(Duration.ofSeconds(5L))
            ObjectMapper().writeValueAsString(TestUser(1L, "taesu"))
        }
        stringRedisRepository.save("user:10", Duration.ofSeconds(10), compute)

        (1..1000).forEach { _ ->
            stringRedisRepository.get("user:10", Duration.ofSeconds(10), recompute = compute)
            Thread.sleep(Duration.ofMillis(Random.nextDouble(100.0).toLong()))
        }
    }
}

class TestUser(
    val userKey: Long,
    val name: String,
)
