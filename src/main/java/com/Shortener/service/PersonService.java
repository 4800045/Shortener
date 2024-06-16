package com.Shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shortener.models.Person;
import com.Shortener.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    
    @Autowired
    public PersonService(PersonRepository personRepository) {
	this.personRepository = personRepository;
    }
    
    @Transactional
    public void save(Person person) {
	personRepository.save(person);
    }
    
    
}
