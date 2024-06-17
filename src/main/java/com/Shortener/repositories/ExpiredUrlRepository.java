package com.Shortener.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Shortener.models.ExpiredUrl;

public interface ExpiredUrlRepository extends JpaRepository<ExpiredUrl, Integer>{
    public Optional<ExpiredUrl> findByLongUrl(String longUrl);

}
