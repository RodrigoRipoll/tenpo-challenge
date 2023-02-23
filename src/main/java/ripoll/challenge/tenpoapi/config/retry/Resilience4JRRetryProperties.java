package ripoll.challenge.tenpoapi.config.retry;

import lombok.Data;

@Data
public class Resilience4JRRetryProperties {

  private Integer maxAttempts;
  private Integer exponentialBackOffIntervalMillis;
  private Integer exponentialBackOffMultiplier;

}
