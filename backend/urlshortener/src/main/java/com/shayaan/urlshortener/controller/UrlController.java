// This controller handles HTTP requests/API endpoints
// Currently out of CRUD, is only CR
// -> POST /api/shorten creates a short URL
// -> GET /{shortCode} redirects to the original URL
// Updated to allow dynamic URL for host in case I change later on
package com.shayaan.urlshortener.controller;

import com.shayaan.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    // This allows us to configure the base URL through environment variables
    @Value("${app.base-url:}")
    private String baseUrl;

    @PostMapping("/api/shorten")
    public ResponseEntity<String> shorten(@RequestBody String originalUrl, HttpServletRequest request) {
        String shortCode = urlService.shortenUrl(originalUrl.trim()).trim();
        System.out.println("the short code is [" + shortCode + "]");

        // Use configured base URL or build it from the request
        String finalBaseUrl = !baseUrl.isEmpty() ? baseUrl :
                request.getScheme() + "://" + request.getServerName() +
                        (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");

        String cleanUrl = finalBaseUrl + "/" + shortCode;
        System.out.println("Final redirect URL: " + cleanUrl);
        return ResponseEntity.ok(cleanUrl);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        return urlService.getOriginalUrl(shortCode)
                .map(url -> ResponseEntity.status(302).header("Location", url).build())
                .orElse(ResponseEntity.notFound().build());
    }
}