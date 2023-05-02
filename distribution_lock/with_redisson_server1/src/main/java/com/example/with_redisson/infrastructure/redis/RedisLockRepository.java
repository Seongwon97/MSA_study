package com.example.with_redisson.infrastructure.redis;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // setIfAbsent() 를 활용해서 SETNX를 실행한다.
    // key는 Party테이블의 PK이며 Value를 "lock" 으로 설정한다.
    public Boolean lock(long key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(String.valueOf(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(long key) {
        return redisTemplate.delete(String.valueOf(key));
    }
}
