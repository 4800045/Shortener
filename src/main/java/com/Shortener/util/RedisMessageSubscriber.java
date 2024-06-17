package com.Shortener.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.Shortener.models.UsersUrl;
import com.Shortener.service.UrlService;

@Component
public class RedisMessageSubscriber implements MessageListener{
    
    private final UrlService urlService;

    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    public RedisMessageSubscriber(UrlService urlService, RedisTemplate<String, String> redisTemplate) {
	this.urlService = urlService;
	this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
	String expiredKey = new String(message.getBody());
	
	System.out.println("Key expired: " + expiredKey);
	
	Optional<UsersUrl> usersUrl = urlService.findByShortUrl(expiredKey);
	
	urlService.saveToExpired(usersUrl.get());
	
	urlService.delete(usersUrl.get());
	
	
	
    }
    
    public void subscribeToExpirationEvents() {
	redisTemplate.getConnectionFactory().getConnection().subscribe(this, "__keyevent@0__:expired".getBytes());
    }
    
    
    
}
