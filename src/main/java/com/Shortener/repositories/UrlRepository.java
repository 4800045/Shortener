package com.Shortener.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Shortener.models.UsersUrl;

@Repository
public interface UrlRepository extends JpaRepository<UsersUrl, Integer>{

    public Optional<UsersUrl> findByShortUrl(String shortUrl);
    
    public List<UsersUrl> findByPersonId(int id);
    
    public Optional<UsersUrl> findByLongUrl(String longUrl);
    
    public void deleteById(long id);
}
