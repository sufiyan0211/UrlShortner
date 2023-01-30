package com.urlShortner.urlShortner.repository.slave;

import com.urlShortner.urlShortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaveUrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}

