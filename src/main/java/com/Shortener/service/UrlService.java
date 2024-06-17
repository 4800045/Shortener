package com.Shortener.service;

import java.util.List;
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
	
	Optional<UsersUrl> usersUrl = urlRepository.findByShortUrl(shortUrl);
	
	
	if (usersUrl.isPresent()) {
	    
	    return;
	}
	
	UsersUrl url = new UsersUrl();
	
	url.setLongUrl(longUrl);
	url.setShortUrl(shortUrl);
	url.setPerson(person);
	
	urlRepository.save(url);
    }
    
    @Transactional
    public void save(String longUrl, String shortUrl) {
	
	
	Optional<UsersUrl> usersUrl = urlRepository.findByShortUrl(shortUrl);
	
	if (usersUrl.isPresent()) {
	    
	    return;
	}
	
	UsersUrl url = new UsersUrl();
	
	url.setLongUrl(longUrl);
	url.setShortUrl(shortUrl);
	
	urlRepository.save(url);
    }
    
    public Optional<UsersUrl> findByShortUrl(String shortUrl) {
	return urlRepository.findByShortUrl(shortUrl);
    }
    
    public Optional<UsersUrl> findByLongUrl(String longUrl) {
	return urlRepository.findByLongUrl(longUrl);
    }
    
    public Optional<ExpiredUrl> findByLongUrlInExpired(String longUrl) {
	return expiredUrlRepository.findByLongUrl(longUrl);
    }
    
    public List<UsersUrl> urlListForPerson(int id) {
	return urlRepository.findByPersonId(id);
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
    
    @Transactional
    public void deleteUrlFromUserPage(String shortUrl) {
	Optional<UsersUrl> url = urlRepository.findByShortUrl(shortUrl);
	
	saveToExpired(url.get());
	
	long urlId = url.get().getId();
	
	urlRepository.deleteById(urlId);
	
	
	
    }
    
    
    
    
}