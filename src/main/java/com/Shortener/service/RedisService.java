package com.Shortener.service;

import org.springframework.stereotype.Service;

import com.Shortener.models.ExpiredUrl;
import com.Shortener.models.UsersUrl;

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
    private final RedisTemplate redisTemplate;
    
    @Autowired
    public RedisService(UrlService urlService, RedisTemplate<String, Object> redisTemplate) {
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
	    
	    Map<String, Object> data = new HashMap<>();
	    
	    data.put("longUrl", longUrl);
	    data.put("totalClicks", 0);
	    
	    redisTemplate.opsForHash().putAll(expiredUrl.get().getShortUrl(), data);
	    redisTemplate.expire(expiredUrl.get().getShortUrl(), timeout, TimeUnit.SECONDS);
	    
	    urlService.deleteFromExpired(expiredUrl.get());
	    
	    return expiredUrl.get().getShortUrl();
	}
	
	else if (usersUrl.isPresent()) {
	    return usersUrl.get().getShortUrl();
	}
	
	String shortUrl = generateShortUrl(longUrl);
		
	Map<String, Object> urlData = new HashMap<>();
	
	urlData.put("longUrl", longUrl);
	urlData.put("totalClicks", 0);
	
	redisTemplate.opsForHash().putAll(shortUrl, urlData);
	redisTemplate.expire(shortUrl, timeout, TimeUnit.SECONDS);
	
	
	
	return shortUrl;
    }
    
    public Map<String, Object> getData(String shortUrl) {
	return redisTemplate.opsForHash().entries(shortUrl);
    }
    
    
    public String getUrl(String shortUrl) {
	return (String) getData(shortUrl).get("longUrl");
    }
    
    public void deleteFromRedis(String key) {
	redisTemplate.delete(key);
	
    }
    
    
    public List<UsersUrl> getTotalClicks(List<UsersUrl> urlList) {
	
	for (UsersUrl url : urlList) {
	    Map<String, Object> data = getData(url.getShortUrl());
	    url.setTotalClicks((Integer) data.get("totalClicks"));
	}
	
	return urlList;
	
    }
}
