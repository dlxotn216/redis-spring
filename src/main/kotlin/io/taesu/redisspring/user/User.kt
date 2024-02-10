package io.taesu.redisspring.user

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by itaesu on 2024/02/09.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "user_key", unique = true, nullable = false)
    val userKey: Long = 0L,
    @Column(name = "user_id", unique = true, nullable = false)
    val userId: String,
    @Column(name = "name", nullable = false)
    var name: String,
)

interface UserEntityRepository: JpaRepository<User, Long>
