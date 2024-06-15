package com.Shortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.Shortener.models.Person;
import com.Shortener.repositories.PersonRepository;
import com.Shortener.security.PersonDetails;

@Service
public class PersonDetailsService implements UserDetailsService{

    private final PersonRepository personRepository;
    
    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
	this.personRepository = personRepository;
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<Person> person = personRepository.findByUsername(username);
	
	if (person.isEmpty()) {
	    throw new UsernameNotFoundException("User not found");
	}
	
	return new PersonDetails(person.get());
	
    }

}
