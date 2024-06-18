package com.Shortener.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Shortener.security.JWTUtil;
import com.Shortener.service.PersonDetailsService;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{
    
    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;
    
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
	this.jwtUtil = jwtUtil;
	this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {
		
	System.out.println("doFilterInternal started");
	
	String authHeader = request.getHeader("Authorization");
	
	if (authHeader != null && authHeader.isBlank() && authHeader.startsWith("Bearer")) {
	    String jwt = authHeader.substring(7);
	    
	    if (jwt.isBlank()) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
	    }
	    else {
		try {
		    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
		    
		    UserDetails userDetails = personDetailsService.loadUserByUsername(username);
		    
		    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
			    userDetails.getPassword(), userDetails.getAuthorities());
		    
		    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
		}
		catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid JWT Token");
                }
	    }
	}
	System.out.println("doFilterInternal done");
	
	filterChain.doFilter(request, response);
    }

}
