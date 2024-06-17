package com.Shortener.service;

import org.springframework.stereotype.Service;

import com.Shortener.models.ExpiredUrl;
import com.Shortener.models.UsersUrl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class RedisService {
    
    private final UrlService urlService;
    private RedisTemplate<String, String> redisTemplate;

    
    @Autowired
    public RedisService(UrlService urlService, RedisTemplate<String, String> redisTemplate) {
	this.urlService = urlService;
	this.redisTemplate = redisTemplate;
    }
    
    
    
    
    private String generateShortUrl(String longUrl) {
	return Integer.toHexString(longUrl.hashCode());
    }
    
    public String shortenUrl(String longUrl, long timeout) {
	
	Optional<ExpiredUrl> expiredUrl = urlService.findByLongUrlInExpired(longUrl);
	Optional<UsersUrl> usersUrl = urlService.findByShortUrl(longUrl);
	
	if (expiredUrl.isPresent()) {
	    redisTemplate.opsForValue().set(expiredUrl.get().getShortUrl(), expiredUrl.get().getLongUrl(), timeout, TimeUnit.SECONDS);
	    
	    urlService.deleteFromExpired(expiredUrl.get());
	    
	    return expiredUrl.get().getShortUrl();
	}
	
	else if (usersUrl.isPresent()) {
	    return usersUrl.get().getShortUrl();
	}
	
	String shortUrl = generateShortUrl(longUrl);
		
	redisTemplate.opsForValue().set(shortUrl, longUrl, timeout, TimeUnit.SECONDS);
	
	return shortUrl;
    }
    
    public String getUrl(String url) {
	return redisTemplate.opsForValue().get(url);
    }
    
    public void deleteFromRedis(String key) {
	redisTemplate.delete(key);
	
    }
    
    
    
}
