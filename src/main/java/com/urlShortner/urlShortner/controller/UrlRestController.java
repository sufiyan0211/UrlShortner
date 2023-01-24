package com.urlShortner.urlShortner.controller;

import com.urlShortner.urlShortner.model.ResponseBody;
import com.urlShortner.urlShortner.model.Url;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class UrlRestController {
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


    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(shortLink))
        {
            ResponseBody responseBody = new ResponseBody();
            responseBody.setError("Invalid Url");
            responseBody.setStatus("400");
            return new ResponseEntity<ResponseBody>(responseBody, HttpStatus.OK);
        }

        Url url = urlService.getUrlObject(shortLink);

        if(url == null)
        {
            ResponseBody responseBody = new ResponseBody();
            responseBody.setError("Url does not exist or it might have expired!");
            responseBody.setStatus("400");
            return new ResponseEntity<ResponseBody>(responseBody,HttpStatus.OK);
        }

        LocalDate todayDate = LocalDate.now();
        LocalDate createdDate = url.getCreatedDate();
        long daysBetween = ChronoUnit.DAYS.between(createdDate, todayDate);

        if(daysBetween > 7)
        {
            urlService.deleteUrl(shortLink);
            ResponseBody responseBody = new ResponseBody();
            responseBody.setError("Url Expired. Please try generating a fresh one.");
            responseBody.setStatus("200");
            return new ResponseEntity<ResponseBody>(responseBody,HttpStatus.OK);
        }

        response.sendRedirect(url.getLongUrl());
        return null;
    }


}
