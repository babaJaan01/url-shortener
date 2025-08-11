// This controller handles HTTP requests/API endpoints
// Currently out of CRUD, is only CR
// -> POST /api/shorten creates a short URL
// -> GET /{shortCode} redirects to the original URL
// Updated to allow dynamic URL for host in case I change later on
package com.shayaan.urlshortener.controller;

import com.shayaan.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<String> shorten(@RequestBody String originalUrl) {
        String shortCode = urlService.shortenUrl(originalUrl.trim());
        return ResponseEntity.ok(shortCode);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        return urlService.getOriginalUrl(shortCode)
                .map(url -> ResponseEntity.status(302).header("Location", url).build())
                .orElse(ResponseEntity.notFound().build());
    }
}