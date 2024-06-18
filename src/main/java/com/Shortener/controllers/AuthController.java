package com.Shortener.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Shortener.dto.AuthDTO;
import com.Shortener.models.Person;
import com.Shortener.security.JWTUtil;
import com.Shortener.service.PersonService;

@RestController
public class AuthController {

    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;
    private final PersonService personService;
    
    @Autowired
    public AuthController(AuthenticationManager authManager, JWTUtil jwtUtil, PersonService personService) {
	this.authManager = authManager;
	this.jwtUtil = jwtUtil;
	this.personService = personService;
    }
    
    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody Person person
                                      ) {


	personService.save(person);

        String token = jwtUtil.generateToken(person.getUsername());
        
        System.out.println("Got a token after registation: " + token);
        
        return Map.of("jwt-token", token);
    }
    		
    
    
    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> performLogin(@RequestBody AuthDTO authDTO) {
	
	
	UsernamePasswordAuthenticationToken authToken = 
		new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
	
	try {
	   authManager.authenticate(authToken);
	}
	catch (BadCredentialsException e) {
	    return Map.of("message", "Incorrect credentials");
	}
	
	String token = jwtUtil.generateToken(authDTO.getUsername());
	
	
	
	return Map.of("jwt-token", token);
	
	
    }
    
}
