package com.Shortener.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Shortener.models.Person;


public class PersonDetails implements UserDetails{
    
    private final Person person;
    
    public PersonDetails(Person person) {
	this.person = person;
    }
    	
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return null;
    }

    @Override
    public String getPassword() {
	return this.person.getPassword();
    }

    @Override
    public String getUsername() {
	return this.person.getUsername();
    }
    
    public Person getPerson() {
	return this.person;
    }

}
