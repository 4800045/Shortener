package com.Shortener.controllers;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.Shortener.models.Person;
import com.Shortener.security.PersonDetails;
import com.Shortener.service.PersonService;
import com.Shortener.service.RedisService;
import com.Shortener.service.UrlService;

@Controller
public class MainController {

    private final RedisService redisService;
    private final PersonService personService;
    private final UrlService urlService;
    
    @Autowired
    public MainController(RedisService redisService, PersonService personService, UrlService urlService) {
	this.redisService = redisService;
	this.personService = personService;
	this.urlService = urlService;
    }
    
    @GetMapping()
    public String homePage() {
	return "homePage";
    }
    
    @PostMapping("/shorten")
    public String shorten(@RequestParam("url") String url, Model model) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
	
	String shortUrl = redisService.shortenUrl(url, 240);
	urlService.save(url, shortUrl, personDetails.getPerson());
	
	model.addAttribute("shortUrl", shortUrl);
	
	
	
	
	return "shortUrlPage";
	
    }
    
    @GetMapping("/r/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
	String longUrl = redisService.getUrl(shortUrl);
	
	return new RedirectView(longUrl);
    }
    
    
    
    
    
    
    
    
    
    
}
