package com.urlshortener.repository;

import com.urlshortener.entity.UrlStats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlStatsRepository extends CassandraRepository<UrlStats, String> {
  @Query("update urls_stats set click_count = click_count + ?1 where short_url = ?0")
  void updateClicks(String shortUrl, Long clicks);

  @Query("update urls_stats set click_count = click_count + 1 where short_url = ?0")
  void incrementClicks(String shortUrl);
}
