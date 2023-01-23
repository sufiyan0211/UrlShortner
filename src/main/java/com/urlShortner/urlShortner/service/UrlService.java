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
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    Logger logger = LoggerFactory.getLogger(UrlService.class);

    public ResponseBody createShortUrl(UrlRequestBody urlRequestBody) {
        logger.info("Creating Short Url of " + urlRequestBody.getLongUrl());
        ResponseBody responseBody = new ResponseBody();
        if(StringUtils.isEmpty(urlRequestBody.getLongUrl())) {
            logger.warn("Request Long Url is empty");
            responseBody.setStatus("404");
            responseBody.setError("Please provide the valid url");
            return responseBody;
        }
        logger.info("Encoding the Long Url " + urlRequestBody.getLongUrl());
        String encodeUrl = encodeUrl(urlRequestBody.getLongUrl());
        logger.info("Encoded Url is: " + encodeUrl);
        List<Url> allUrls =  listAllUrls();
        Url url = urlRepository.findByShortUrl(encodeUrl);
        if(allUrls.contains(url)) {
            logger.warn("Long Url already existed in the Database");
            responseBody.setStatus("200");
            responseBody.setUrl(url);
        }
        else {
            url = new Url();
            url.setLongUrl(urlRequestBody.getLongUrl());
            url.setShortUrl(encodeUrl);
            url.setCreatedDate(LocalDate.now());
            logger.info("Storing the long Url and short url into the database");
            urlRepository.save(url);
            responseBody.setStatus("200");
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

    private String encodeUrl(String url)
    {
        String encodedUrl = "";
        encodedUrl = Hashing.murmur3_32()
                        .hashString(url, StandardCharsets.UTF_8)
                        .toString();
        return encodedUrl;
    }

}
