package com.urlShortner.urlShortner.repository.master;

import com.urlShortner.urlShortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterUrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}
