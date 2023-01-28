package com.urlShortner.urlShortner.service;

import com.google.common.hash.Hashing;
import com.urlShortner.urlShortner.model.ResponseBody;
import com.urlShortner.urlShortner.model.Url;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.repository.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UrlService {
    Logger logger = LoggerFactory.getLogger(UrlService.class);
    @Autowired
    private UrlRepository urlRepository;

    public ResponseBody createShortUrl(UrlRequestBody urlRequestBody) {
        logger.info("Creating Short Url of " + urlRequestBody.getLongUrl());
        ResponseBody responseBody = new ResponseBody();
        if (StringUtils.isEmpty(urlRequestBody.getLongUrl())) {
            logger.warn("Request Long Url is empty");
            responseBody.setStatus("404");
            responseBody.setError("Please provide the valid url");
            return responseBody;
        }
        logger.info("Encoding the Long Url " + urlRequestBody.getLongUrl());
        String encodeUrl = encodeUrl(urlRequestBody.getLongUrl());
        logger.info("Encoded Url is: " + encodeUrl);
        List<Url> allUrls = listAllUrls();
        Url url = urlRepository.findByShortUrl(encodeUrl);
        if (allUrls.contains(url)) {
            // url already existed in the db
            LocalDate todayDate = LocalDate.now();
            LocalDate createdDate = url.getCreatedDate();
            long daysBetween = ChronoUnit.DAYS.between(createdDate, todayDate);
            if (daysBetween > 7) {
                String createdDateStr = String.format("Created Url: %s", url.getCreatedDate().toString());
                logger.info(createdDateStr);
                logger.warn("Short url already existed in the database and it is expired, deleting the url now");
                urlRepository.delete(url);
                responseBody.setError("Url Expired. Please try generating a fresh one.");
                responseBody.setStatus("500");
            }
            else {
                logger.warn("Long Url already existed in the Database");
                responseBody.setStatus("200");
                responseBody.setUrl(url);
            }
        } else {
            // url does not already exist in the db
            url = new Url();
            url.setLongUrl(urlRequestBody.getLongUrl());
            url.setShortUrl(encodeUrl);
            url.setCreatedDate(LocalDate.now());
            logger.info("Storing the long Url and short url into the database");
            urlRepository.save(url);
            responseBody.setStatus("200");
            responseBody.setError("Successfully");
            responseBody.setUrl(url);
        }
        return responseBody;
    }

    public List listAllUrls() {
        return urlRepository.findAll();
    }

    public void deleteUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        logger.info("Deleting the shortUrl: " + shortUrl);
        urlRepository.delete(url);
    }

    public void deleteAll() {
        logger.info("Deleting all the URLs from database");
        urlRepository.deleteAll();
    }

    public Url getUrlObject(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl);
        return url;
    }

    private String encodeUrl(String url) {
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32()
                .hashString(url, StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }

}
