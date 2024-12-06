package com.urlshortener.dto;

import lombok.Data;

@Data
public class UrlResponse {
  private String message;
  private String shortUrl;
}
