package com.cm.account.validator.nonreactive.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("config")
public class SourceEndpointProperty {
  private List<Source> providers;

  @Data
  public static class Source {
    private String name;
    private String url;
  }
}
