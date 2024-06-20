package com.Shortener.controllers;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.Shortener.dto.AuthDTO;
import com.Shortener.models.Person;
import com.Shortener.models.UsersUrl;
import com.Shortener.security.PersonDetails;
import com.Shortener.service.PersonService;
import com.Shortener.service.RedisService;
import com.Shortener.service.UrlService;
import com.Shortener.util.AuthCheck;

@Controller
public class MainController {

    private final RedisService redisService;
    private final PersonService personService;
    private final UrlService urlService;
    private final AuthCheck authCheck;
    
    @Autowired
    public MainController(RedisService redisService, PersonService personService, UrlService urlService, AuthCheck authCheck) {
	this.redisService = redisService;
	this.personService = personService;
	this.urlService = urlService;
	this.authCheck = authCheck;
    }
    
    @GetMapping("/auth/login")
    public String loginPage() {
	return "loginPage";
    }
    
    @GetMapping()
    public String homePage(Model model) {
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	
	if (authCheck.check(authentication)) {
	    System.out.println("auth OK");

	    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		
		
	    List<UsersUrl> urls = urlService.urlListForPerson(personDetails.getPerson().getId());
	    
	    for(UsersUrl url : urls) {
		System.out.println(url.getLongUrl());
	    }
	    
	    List<UsersUrl> urlList = redisService.getTotalClicks(urls);
	    
	    for(UsersUrl url : urlList) {
		System.out.println(url.getTotalClicks());
	    }
	    
		
	    model.addAttribute("urlList", urlList);
	    
	}
	return "homePage";
    }
    
    @PostMapping("/shorten")
    public String shorten(@RequestParam("url") String url, Model model) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	
	String shortUrl = redisService.shortenUrl(url, 1000);
	
	if (authCheck.check(authentication)) {
	    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
	    
		
	    urlService.save(url, shortUrl, personDetails.getPerson());
	    
	}
	else {
	    urlService.save(url, shortUrl);
	}
	
	
	model.addAttribute("shortUrl", shortUrl);
	
	
	return "shortUrlPage";
	
    }
    
    @GetMapping("/r/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
	String longUrl = redisService.getUrl(shortUrl);
	
	return new RedirectView(longUrl);
    }
    
    @PostMapping("/deleteToken")
    public String deleteToken(@RequestParam("url") String url) {
    	redisService.deleteFromRedis(url);
    
    	urlService.deleteUrlFromUserPage(url);
    	
    	return "redirect:/";
    }    
    
    
    
    
    
    
    
    
    
}
