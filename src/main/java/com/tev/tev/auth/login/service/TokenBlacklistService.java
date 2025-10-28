package com.tev.tev.auth.login.service;

import com.tev.tev.auth.login.jwt.TokenProvider;
import com.tev.tev.redis.dao.RedisDao;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addBlacklist(String token){
        String key = BLACKLIST_PREFIX + token;

        // key 조회용
        Claims claims = tokenProvider.parseClaims(token);
        Date expiration = claims.getExpiration();
        long now = new Date().getTime();
        long remainingExpiration = expiration.getTime() - now;
        if(remainingExpiration > 0){
            redisDao.setValues(key, "", Duration.ofMillis(remainingExpiration));
        }
    }

    public boolean isBlacklisted(String token){
        String key = BLACKLIST_PREFIX + token;
        return redisDao.getValues(key) != null;
    }
}
