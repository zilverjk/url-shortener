package com.urlshortener.controller;

import com.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UrlController {
  private final UrlShortenerService urlShortenerService;

  @Autowired
  public UrlController(UrlShortenerService urlShortenerService) {
    this.urlShortenerService = urlShortenerService;
  }

  @PostMapping("/shorten")
  public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
    String shortUrl = this.urlShortenerService.buildShorUrl(longUrl);

    return ResponseEntity.ok(shortUrl);
  }

  @GetMapping("/{shortUrl}")
  public ResponseEntity<String> redirectToLongUrl(@PathVariable String shortUrl) {
    String longUrl = this.urlShortenerService.getLongUrl(shortUrl);

    if (longUrl == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(longUrl);
  }

  @GetMapping("/{shortUrl}/stats")
  public ResponseEntity<Long> getUrlStats(@PathVariable String shortUrl) {
    Long clicksCount = this.urlShortenerService.getClicksCount(shortUrl);

    return ResponseEntity.ok(clicksCount);
  }
}
