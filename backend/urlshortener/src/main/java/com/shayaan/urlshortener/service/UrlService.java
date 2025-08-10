// This is the business logic. Creating the actual short url codes

package com.shayaan.urlshortener.service;

import com.shayaan.urlshortener.model.UrlMapping;
import com.shayaan.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {
    @Autowired
    private UrlMappingRepository repository;

    public String shortenUrl(String originalUrl) {
        originalUrl = originalUrl.trim();
        if (!originalUrl.toLowerCase().startsWith("http://") && !originalUrl.toLowerCase().startsWith("https://")) {
            originalUrl = "http://" + originalUrl;
        }
        String shortCode = UUID.randomUUID().toString().substring(0, 6).trim();
        UrlMapping mapping = new UrlMapping(null, shortCode, originalUrl);
        repository.save(mapping);
        return shortCode;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode).map(UrlMapping::getOriginalUrl);
    }
}
