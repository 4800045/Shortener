package com.Shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

import com.Shortener.service.PersonDetailsService;

@Configuration
public class SecurityConfig {
    
    private final PersonDetailsService personDetailsService;
    
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
	this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(personDetailsService);
    }
    
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	http.csrf(csrf -> csrf.disable())
		.authorizeRequests(authorizeRequests -> 
			authorizeRequests.anyRequest().permitAll());
	
	return http.build();
		
	
    }
    
    
    
}
