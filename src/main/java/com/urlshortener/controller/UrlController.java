package com.urlshortener.controller;

import com.urlshortener.dto.UrlRequest;
import com.urlshortener.dto.UrlResponse;
import com.urlshortener.service.UrlShortenerService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {
    String url = request.getLongUrl().trim();
    UrlResponse response = new UrlResponse();

    if (url.isEmpty()) {
      response.setMessage("A valid URL is required");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    if (!url.matches("^(http|https)://.*")) {
      response.setMessage("Invalid URL format");

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    String shortUrl = this.urlShortenerService.buildShorUrl(request.getLongUrl().trim());

    response.setMessage("URL shortened successfully");
    response.setShortUrl(shortUrl);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{shortUrl}")
  public ResponseEntity<UrlResponse> redirectToLongUrl(@PathVariable String shortUrl) {
    String longUrl = this.urlShortenerService.getLongUrl(shortUrl);

    if (longUrl == null) {
      return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(longUrl.trim()));

    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/{shortUrl}/stats")
  public ResponseEntity<Long> getUrlStats(@PathVariable String shortUrl) {
    Long clicksCount = this.urlShortenerService.getClicksCount(shortUrl);

    return ResponseEntity.ok(clicksCount);
  }
}
