package com.urlShortner.urlShortner.controller;

import com.urlShortner.urlShortner.model.ResponseBody;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Value;


@Controller
public class UrlController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("urlRequestBodyForm", new UrlRequestBody());
        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@ModelAttribute UrlRequestBody urlRequestBody, Model model) {
        ResponseBody responseBody = urlService.createShortUrl(urlRequestBody);
        model.addAttribute("originalUrl", responseBody.getUrl().getLongUrl());

        String shortUrl = responseBody.getUrl().getShortUrl();
        String shortUrlView = "";
        if("prod".equals(activeProfile)) {
            shortUrlView = "http://short.sufiyandev.com/";
            shortUrlView += shortUrl;
        }
        else {
            shortUrlView = "http://localhost:9090/";
            shortUrlView += shortUrl;
        }
        model.addAttribute("shortenedUrl", shortUrlView);

        return "view";
    }

}
