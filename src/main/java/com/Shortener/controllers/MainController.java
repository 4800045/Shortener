package com.Shortener.controllers;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.Shortener.service.RedisService;

@Controller
public class MainController {

    private final RedisService redisService;
    
    @Autowired
    public MainController(RedisService redisService) {
	this.redisService = redisService;
    }
    
    @GetMapping()
    public String homePage() {
	return "homePage";
    }
    
    @PostMapping("/shorten")
    public String shorten(@ModelAttribute("url") String url, Model model) {
	String shortUl = redisService.shortenUrl(url, 10000);
	
	model.addAttribute("shortUrl", shortUl);
	
	return "shortUrlPage";
	
    }
    
    @GetMapping("/r/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
	String longUrl = redisService.getUrl(shortUrl);
	
	return new RedirectView(longUrl);
    }
    
    
    
    
    
    
    
}
