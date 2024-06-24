package com.Shortener.service;

import org.springframework.stereotype.Service;

import com.Shortener.models.ExpiredUrl;
import com.Shortener.models.UsersUrl;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class RedisService {
    
    private final UrlService urlService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Long> redisTemplate2;
    
    @Autowired
    public RedisService(UrlService urlService, RedisTemplate<String, Object> redisTemplate, RedisTemplate<String, Long> redisTemplate2) {
	this.urlService = urlService;
	this.redisTemplate = redisTemplate;
	this.redisTemplate2 = redisTemplate2;
    }
    
    
    
    
    private String generateShortUrl(String longUrl) {
	return Integer.toHexString(longUrl.hashCode());
    }
    
    public String shortenUrl(String longUrl, long timeout) {
	
	Optional<ExpiredUrl> expiredUrl = urlService.findByLongUrlInExpired(longUrl);
	Optional<UsersUrl> usersUrl = urlService.findByShortUrl(longUrl);
	
	
	if (expiredUrl.isPresent()) {
	    redisTemplate.opsForValue().set("ShortUrl:" + expiredUrl.get().getShortUrl(), longUrl, timeout, TimeUnit.SECONDS);
	    
	    redisTemplate2.opsForValue().set("TotalClicks:" + expiredUrl.get().getShortUrl(), 0L);
	    
	    urlService.deleteFromExpired(expiredUrl.get());
	    
	    return expiredUrl.get().getShortUrl();
	}
	
	else if (usersUrl.isPresent()) {
	    return usersUrl.get().getShortUrl();
	}
	
	String shortUrl = generateShortUrl(longUrl);
	
	System.out.println("Generated short URL token: " + shortUrl);
	
	redisTemplate.opsForValue().set("ShortUrl:" + shortUrl, longUrl, timeout, TimeUnit.SECONDS);
	    
	redisTemplate2.opsForValue().set("TotalClicks:" + shortUrl, 0L);	
	
	
	return shortUrl;
    }

    
    public String getUrl(String shortUrl) {
	return (String) redisTemplate.opsForValue().get("ShortUrl:" + shortUrl);
    }
    
    public void deleteFromRedis(String key) {
	redisTemplate.delete("ShortUrl:" + key);
	
	redisTemplate.delete("TotalClicks:" + key);
	redisTemplate.delete("UniqueVisitors:" + key);
	
    }
    
    public void totalClicksInc(String shortUrl) {
	redisTemplate2.opsForValue().increment("TotalClicks:" + shortUrl);
    }
    
    
    public Long getTotalClicks(String shortUrl) {
	return (Long) redisTemplate2.opsForValue().get("TotalClicks:" + shortUrl);
    }
     
    public void recordVisit(HttpServletRequest request, String shortUrl) {
	String ipAddress = request.getRemoteAddr();
	
	System.out.println("Ip address from service: " + ipAddress);
	
	redisTemplate.opsForSet().add("UniqueVisitors:" + shortUrl, ipAddress);
    }
    
    public Long getUniqueVisitorsCount(String shortUrl) {
	return redisTemplate.opsForSet().size("UniqueVisitors:" + shortUrl);
    }
}
