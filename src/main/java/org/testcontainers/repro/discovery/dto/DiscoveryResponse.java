package org.testcontainers.repro.discovery.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(
  builder = DiscoveryResponse.Builder.class
)
@Data
public final class DiscoveryResponse {
  private final Boolean known;
  private final Boolean present;
  private final String message;
  @JsonProperty("app_name")
  private final String appName;
  @JsonProperty("endpoint_url")
  private final String url;
  @JsonProperty("cache_time")
  private final String cacheTime;
  @JsonProperty("served_from")
  private final String servedFrom;

  DiscoveryResponse(Boolean known, Boolean present, String message, String appName, String url, String cacheTime, String servedFrom) {
    this.known = known;
    this.present = present;
    this.message = message;
    this.appName = appName;
    this.url = url;
    this.cacheTime = cacheTime;
    this.servedFrom = servedFrom;
  }

  public static DiscoveryResponse.Builder builder() {
    return new DiscoveryResponse.Builder();
  }

  public DiscoveryResponse.Builder toBuilder() {
    return (new DiscoveryResponse.Builder()).known(this.known).present(this.present).message(this.message).appName(this.appName).url(this.url).cacheTime(this.cacheTime).servedFrom(this.servedFrom);
  }

  @JsonPOJOBuilder(
    withPrefix = ""
  )
  public static class Builder {
    private Boolean known;
    private Boolean present;
    private String message;
    private String appName;
    private String url;
    private String cacheTime;
    private String servedFrom;

    Builder() {
    }

    public DiscoveryResponse.Builder known(Boolean known) {
      this.known = known;
      return this;
    }

    public DiscoveryResponse.Builder present(Boolean present) {
      this.present = present;
      return this;
    }

    public DiscoveryResponse.Builder message(String message) {
      this.message = message;
      return this;
    }

    @JsonProperty("app_name")
    public DiscoveryResponse.Builder appName(String appName) {
      this.appName = appName;
      return this;
    }

    @JsonProperty("endpoint_url")
    public DiscoveryResponse.Builder url(String url) {
      this.url = url;
      return this;
    }

    @JsonProperty("cache_time")
    public DiscoveryResponse.Builder cacheTime(String cacheTime) {
      this.cacheTime = cacheTime;
      return this;
    }

    @JsonProperty("served_from")
    public DiscoveryResponse.Builder servedFrom(String servedFrom) {
      this.servedFrom = servedFrom;
      return this;
    }

    public DiscoveryResponse build() {
      return new DiscoveryResponse(this.known, this.present, this.message, this.appName, this.url, this.cacheTime, this.servedFrom);
    }

    public String toString() {
      return "DiscoveryResponse.Builder(known=" + this.known + ", present=" + this.present + ", message=" + this.message + ", appName=" + this.appName + ", url=" + this.url + ", cacheTime=" + this.cacheTime + ", servedFrom=" + this.servedFrom + ")";
    }
  }
}
