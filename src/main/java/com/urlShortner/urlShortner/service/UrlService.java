package com.urlShortner.urlShortner.service;

import com.urlShortner.urlShortner.model.Url;
import com.urlShortner.urlShortner.model.UrlRequestBody;
import com.urlShortner.urlShortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    public Url createShortUrl(UrlRequestBody urlRequestBody) {
        Url url = new Url();
        url.setLongUrl(urlRequestBody.getLongUrl());
        url.setShortUrl(urlRequestBody.getShortUrl());
        url.setCreatedDate(LocalDate.now());
        return urlRepository.save(url);
    }

    public List listAllUrls() {
        return urlRepository.findAll();
    }

}
