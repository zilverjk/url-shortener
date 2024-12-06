package com.urlshortener.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("urls")
public class Url {
  @PrimaryKey
  private String shortUrl;
  private String longUrl;
  private LocalDateTime createdAt;
}
