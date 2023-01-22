package com.urlShortner.urlShortner.repository;

import com.urlShortner.urlShortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {
    public Url findByShortUrl(String shortUrl);
}
