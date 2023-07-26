package forever.backend.redis.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(
    val redisTemplate: RedisTemplate<String, String>,
    val objectMapper: ObjectMapper
) {
    fun setValue(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    fun <T> getValue(key: String, klass: Class<T>): T? {
        return redisTemplate.opsForValue().get(key)?.let {
            objectMapper.readValue(it, klass)
        }
    }

    final inline fun <reified T> getValue(key: String): T? {
        return getValue(key, T::class.java)
    }
}
