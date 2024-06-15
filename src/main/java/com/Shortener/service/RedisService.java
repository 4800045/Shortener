package com.Shortener.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class RedisService {

    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    
    private String generateShortUrl(String longUrl) {
	return Integer.toHexString(longUrl.hashCode());
    }
    
    public String shortenUrl(String longUrl, long timeout) {
	String shortUrl = generateShortUrl(longUrl);
	
	redisTemplate.opsForValue().set(longUrl, shortUrl, timeout, TimeUnit.SECONDS);
	
	redisTemplate.opsForValue().set(shortUrl, longUrl, timeout, TimeUnit.SECONDS);
	
	return shortUrl;
    }
    
    public String getUrl(String url) {
	return redisTemplate.opsForValue().get(url);
    }
    
    
    
}
