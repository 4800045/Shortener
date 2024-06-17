package com.Shortener.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.Shortener.security.PersonDetails;

@Component
public class AuthCheck {

    public boolean check(Authentication authentication) {
	
	
	return (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PersonDetails);
    }
}
