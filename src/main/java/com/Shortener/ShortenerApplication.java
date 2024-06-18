package com.Shortener;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.Shortener.util.RedisMessageSubscriber;

@SpringBootApplication
public class ShortenerApplication implements CommandLineRunner{

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;
    
    public static void main(String[] args) {
	SpringApplication.run(ShortenerApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting subscription to Redis expiration events...");
        redisMessageSubscriber.subscribeToExpirationEvents();
        System.out.println("Subscription to Redis expiration events started.");
    }
    
    @Bean
    public ModelMapper modelMapper() {
	return new ModelMapper();
    }

}
