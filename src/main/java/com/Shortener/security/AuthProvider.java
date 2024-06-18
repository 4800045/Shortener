package com.Shortener.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.Shortener.service.PersonDetailsService;


@Component
public class AuthProvider implements AuthenticationProvider {
    
    private final PersonDetailsService personDetailsService;

    public AuthProvider(PersonDetailsService personDetailsService) {
	this.personDetailsService = personDetailsService;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	String name = authentication.getName();
	
	String password = authentication.getCredentials().toString();
	
	UserDetails personDetails = personDetailsService.loadUserByUsername(name);
	
	if (!personDetails.getPassword().equals(password)) {
	    throw new BadCredentialsException("incorrect password");
	}
	
	
	
	return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
	return true;
    }
    
}

