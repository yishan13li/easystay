package com.easystay.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.easystay.dto.UserDto;
import com.easystay.entity.User;
import com.easystay.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_CACHE_PREFIX = "user:dto:";

    public UserDto getUserDtoById(Long userId) {
        String key = USER_CACHE_PREFIX + userId;

        // 先從 Redis 拿
        UserDto cached = (UserDto) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            System.out.println("從 Redis 快取取得 UserDto");
            return cached;
        }

        System.out.println("從資料庫取得 UserDto");

        // 沒有快取就查 DB
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDto dto = new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPhone(),
            user.getRole(),
            user.getAvatar(),
            user.getStatus(),
            user.getCreatedAt()
        );

        // 存 Redis，30 分鐘過期
        redisTemplate.opsForValue().set(key, dto, Duration.ofMinutes(30));

        return dto;
    }

    public void evictUserCache(Long userId) {
        redisTemplate.delete(USER_CACHE_PREFIX + userId);
    }
}
