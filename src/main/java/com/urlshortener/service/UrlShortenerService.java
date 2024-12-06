package com.urlshortener.service;

import com.google.common.hash.Hashing;
import com.urlshortener.entity.Url;
import com.urlshortener.entity.UrlStats;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.repository.UrlStatsRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {
  private final UrlRepository urlRepository;
  private final UrlStatsRepository urlStatsRepository;
  private final RedisTemplate<String, String> redisTemplate;
  private static final long CACHE_TTL = 24L;
  private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);

  @Autowired
  public UrlShortenerService(
      UrlRepository urlRepository,
      UrlStatsRepository urlStatsRepository,
      RedisTemplate<String, String> redisTemplate) {
    this.urlRepository = urlRepository;
    this.urlStatsRepository = urlStatsRepository;
    this.redisTemplate = redisTemplate;
  }

  public String buildShorUrl(String longUrl) {
    try {
      String shortUrl = Hashing.murmur3_32_fixed()
          .hashString(longUrl + System.currentTimeMillis(), StandardCharsets.UTF_8)
          .toString();

      Url url = new Url();
      url.setShortUrl(shortUrl);
      url.setLongUrl(longUrl);
      url.setCreatedAt(LocalDateTime.now());
      this.urlRepository.save(url);

      UrlStats urlStats = new UrlStats();
      urlStats.setShortUrl(shortUrl);
      urlStats.setClicks(0L); // La L hace referencia a un long, que es un int de 64 bits
      this.urlStatsRepository.save(urlStats);

      redisTemplate.opsForValue().set(shortUrl, longUrl, CACHE_TTL);

      return shortUrl;
    } catch (Exception e) {
      log.error("Error building short URL for long URL: {}", longUrl, e);

      throw new RuntimeException(e);
    }
  }

  public String getLongUrl(String shortUrl) {
    try {
      String urlCached = this.redisTemplate.opsForValue().get(shortUrl);

      if (urlCached != null) {
        incrementClicks(shortUrl);

        return urlCached;
      }

      Optional<Url> url = this.urlRepository.findById(shortUrl);

      if (url.isPresent()) {
        this.redisTemplate.opsForValue().set(shortUrl, url.get().getLongUrl());

        incrementClicks(shortUrl);

        return url.get().getLongUrl();
      }

      return null;
    } catch (Exception e) {
      log.error("Error getting long URL for short URL: {}", shortUrl, e);

      throw new RuntimeException(e);
    }
  }

  public Long getClicksCount(String shortUrl) {
    try {
      Optional<UrlStats> urlStats = this.urlStatsRepository.findById(shortUrl);

      return urlStats.map(UrlStats::getClicks).orElse(0L);
    } catch (Exception e) {
      log.error("Error getting clicks count for short URL: {}", shortUrl, e);

      return 0L;
    }
  }

  private void incrementClicks(String shortUrl) {
    try {
      this.urlStatsRepository.incrementClicks(shortUrl);
    } catch (Exception e) {
      log.error("Error incrementing clicks for URL: {}", shortUrl, e);
    }
  }
}
