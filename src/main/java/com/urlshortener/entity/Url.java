package com.urlshortener.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("urls")
public class Url {
  @PrimaryKey("short_url")
  private String shortUrl;

  @Column("long_url")
  private String longUrl;

  @Column("created_at")
  private LocalDateTime createdAt;
}
