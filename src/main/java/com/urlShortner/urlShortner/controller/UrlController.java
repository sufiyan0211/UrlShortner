package com.urlShortner.urlShortner.controller;

import com.urlShortner.urlShortner.model.Url;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UrlController {
    @Autowired
    private UrlService urlService;

    @GetMapping("/listAllUrls")
    public List listAllUrls() {
        return urlService.listAllUrls();
    }

    @PostMapping("/createShortUrl")
    public Url createShortUrl(@RequestBody UrlRequestBody urlRequestBody) {
        return urlService.createShortUrl(urlRequestBody);
    }
}
