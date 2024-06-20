package com.Shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Shortener.models.Person;
import com.Shortener.service.PersonService;

@Controller
public class PersonController {

    private final PersonService personService;
    
    @Autowired
    public PersonController(PersonService personService) {
	this.personService = personService;
    }
    
    
    
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
	return "registration";
    }
    
//    @PostMapping("/registration")
//    public String registration(@ModelAttribute("person") Person person) {
//	personService.save(person);
//	
//	return "redirect:/login";
//    }
    
}
