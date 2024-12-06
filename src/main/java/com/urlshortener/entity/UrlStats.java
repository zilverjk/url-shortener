package com.urlshortener.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("urls_stats")
public class UrlStats {
  @PrimaryKey
  private String shortUrl;
  private Long clicks;
}
