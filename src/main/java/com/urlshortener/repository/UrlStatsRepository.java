package com.urlshortener.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.urlshortener.entity.UrlStats;

@Repository
public interface UrlStatsRepository extends CassandraRepository<UrlStats, String> {
    @Query("update url_stats set click_count = click_count + 1 where short_url = ?0")
    void incrementClicks(String shortUrl);
}
