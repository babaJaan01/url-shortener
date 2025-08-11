// This is the business logic. Creating the actual short url codes

package com.shayaan.urlshortener.service;

import com.shayaan.urlshortener.model.UrlMapping;
import com.shayaan.urlshortener.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UrlService {
    @Autowired
    private UrlMappingRepository repository;
    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SHORT_CODE_LENGTH = 6;

    public String shortenUrl(String originalUrl) {
        originalUrl = originalUrl.trim();
        if (!originalUrl.toLowerCase().startsWith("http://") && !originalUrl.toLowerCase().startsWith("https://")) {
            originalUrl = "http://" + originalUrl;
        }
        String shortCode = generateUniqueShortCode();
        UrlMapping mapping = new UrlMapping(null, shortCode, originalUrl);
        repository.save(mapping);
        return shortCode;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode).map(UrlMapping::getOriginalUrl);
    }

    private String generateUniqueShortCode() {
        // retry until we find a code that does not exist (collisions are extremely unlikely with 62^6)
        String code;
        do {
            code = randomBase62(SHORT_CODE_LENGTH);
        } while (repository.existsByShortCode(code));
        return code;
    }

    private String randomBase62(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = RANDOM.nextInt(BASE62_ALPHABET.length());
            sb.append(BASE62_ALPHABET.charAt(idx));
        }
        return sb.toString();
    }
}
