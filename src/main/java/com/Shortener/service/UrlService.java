package com.Shortener.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shortener.models.ExpiredUrl;
import com.Shortener.models.Person;
import com.Shortener.models.UsersUrl;
import com.Shortener.repositories.ExpiredUrlRepository;
import com.Shortener.repositories.UrlRepository;

import jakarta.transaction.Transactional;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final ExpiredUrlRepository expiredUrlRepository;
    
    
    public UrlService(UrlRepository urlRepository, ExpiredUrlRepository expiredUrlRepository) {
	this.urlRepository = urlRepository;
	this.expiredUrlRepository = expiredUrlRepository;
    }
    
    @Transactional
    public void save(String longUrl, String shortUrl, Person person) {
	
	UsersUrl usersUrl = new UsersUrl();
	
	usersUrl.setLongUrl(longUrl);
	usersUrl.setShortUrl(shortUrl);
	usersUrl.setPerson(person);
	
	urlRepository.save(usersUrl);
    }
    
    public Optional<UsersUrl> findByShortUrl(String shortUrl) {
	return urlRepository.findByShortUrl(shortUrl);
    }
    
    public Optional<ExpiredUrl> findByLongUrl(String longUrl) {
	return expiredUrlRepository.findByLongUrl(longUrl);
    }
    
    @Transactional
    public void delete(UsersUrl usersUrl) {
	urlRepository.delete(usersUrl);
    }
    
    @Transactional
    public void deleteFromExpired(ExpiredUrl expiredUrl) {
	expiredUrlRepository.delete(expiredUrl);
    }
    
    @Transactional
    public void saveToExpired(UsersUrl usersUrl) {
	ExpiredUrl expiredUrl = new ExpiredUrl();
	
	expiredUrl.setLongUrl(usersUrl.getLongUrl());
	expiredUrl.setShortUrl(usersUrl.getShortUrl());
	
	expiredUrlRepository.save(expiredUrl);
    }
    
    
    
}