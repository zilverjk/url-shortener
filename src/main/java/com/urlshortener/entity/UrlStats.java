package com.urlshortener.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("urls_stats")
public class UrlStats {
  @PrimaryKey("short_url")
  private String shortUrl;

  @Column("clicks")
  private Long clicks;
}
