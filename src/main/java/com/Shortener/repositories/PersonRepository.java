package com.Shortener.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Shortener.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
    public Optional<Person> findByUsername(String username);
}
