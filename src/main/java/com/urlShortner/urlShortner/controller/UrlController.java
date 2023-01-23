package com.urlShortner.urlShortner.controller;

import com.urlShortner.urlShortner.model.ResponseBody;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseBody createShortUrl(@RequestBody UrlRequestBody urlRequestBody) {
        return urlService.createShortUrl(urlRequestBody);
    }

    @RequestMapping(value = "/delete/{shortUrl}", method = RequestMethod.DELETE)
    public void deleteUrl(@PathVariable("shortUrl") String shortUrl) {
        urlService.deleteUrl(shortUrl);
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public void deleteAll() {
        urlService.deleteAll();
    }


}
