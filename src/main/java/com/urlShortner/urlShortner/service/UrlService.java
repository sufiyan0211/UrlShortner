package com.urlShortner.urlShortner.service;

import com.google.common.hash.Hashing;
import com.urlShortner.urlShortner.model.ResponseBody;
import com.urlShortner.urlShortner.model.Url;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.repository.master.MasterUrlRepository;
import com.urlShortner.urlShortner.repository.slave.SlaveUrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UrlService {
    Logger logger = LoggerFactory.getLogger(UrlService.class);
    @Autowired
    private MasterUrlRepository masterUrlRepository;

    @Autowired
    private SlaveUrlRepository slaveUrlRepository;


    public ResponseBody createShortUrl(UrlRequestBody urlRequestBody) {
        logger.info("Creating Short Url of " + urlRequestBody.getLongUrl());
        ResponseBody responseBody = new ResponseBody();
        if (StringUtils.isEmpty(urlRequestBody.getLongUrl())) {
            // when long url is empty
            logger.warn("Request Long Url is empty");
            responseBody.setStatus("404");
            responseBody.setError("Please provide the valid url");
            return responseBody;
        }
        // when long url is not empty
        logger.info("Encoding the Long Url " + urlRequestBody.getLongUrl());
        String encodeUrl = encodeUrl(urlRequestBody.getLongUrl());
        logger.info("Encoded Url is: " + encodeUrl);
        List<Url> allUrls = listAllUrls();
        Url url = getUrlObject(encodeUrl);
        if (allUrls.contains(url)) {
            // when url already existed in the db
            LocalDate todayDate = LocalDate.now();
            LocalDate createdDate = url.getCreatedDate();
            long daysBetween = ChronoUnit.DAYS.between(createdDate, todayDate);
            if (daysBetween > 7) {
                String createdDateStr = String.format("Created Url: %s", url.getCreatedDate().toString());
                logger.info(createdDateStr);
                logger.warn("Short url already existed in the database and it is expired, deleting the url now");
                masterUrlRepository.delete(url);
                slaveUrlRepository.delete(url);
                responseBody.setError("Url Expired. Please try generating a fresh one.");
                responseBody.setStatus("500");
            } else {
                logger.warn("Long Url already existed in the Database");
                responseBody.setStatus("200");
                responseBody.setUrl(url);
            }
            return responseBody;
        }
        // when url does not already exist in the db
        // Now we need to create new Url and save in both DBs
        url = new Url();
        url.setLongUrl(urlRequestBody.getLongUrl());
        url.setShortUrl(encodeUrl);
        url.setCreatedDate(LocalDate.now());
        logger.info("Storing the long Url and short url into the database");
        masterUrlRepository.save(url);
        slaveUrlRepository.save(url);
        responseBody.setStatus("200");
        responseBody.setError("Successfully");
        responseBody.setUrl(url);

        return responseBody;
    }

    public List listAllUrls() {
        return slaveUrlRepository.findAll();
    }

    public void deleteUrl(String shortUrl) {
        Url url = slaveUrlRepository.findByShortUrl(shortUrl);
        logger.info("Deleting the shortUrl: " + shortUrl);
        slaveUrlRepository.delete(url);
        masterUrlRepository.delete(url);
    }

    public void deleteAll() {
        logger.info("Deleting all the URLs from database");
        masterUrlRepository.deleteAll();
        slaveUrlRepository.deleteAll();
    }

    public Url getUrlObject(String shortUrl) {
        Url url = slaveUrlRepository.findByShortUrl(shortUrl);
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
