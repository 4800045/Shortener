package com.Shortener.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.Shortener.service.PersonDetailsService;

@Configuration
public class SecurityConfig {
    
    private final PersonDetailsService personDetailsService;
    
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
	this.personDetailsService = personDetailsService;
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(personDetailsService);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authorize -> authorize
			.requestMatchers("/registration").permitAll()
			.anyRequest().permitAll()
	)
		.formLogin(formLogin -> formLogin
			.defaultSuccessUrl("/", true));	;
	
	return http.build();
		
	
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
	return NoOpPasswordEncoder.getInstance();
    }
    
    
    
}
