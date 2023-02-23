package ripoll.challenge.tenpoapi.config.retry;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfiguration {

  public static final String TAX_INTEGRATION_RETRY = "TAX-INTEGRATION-RETRY";

  @Bean
  @ConfigurationProperties("retry.tax-integration")
  public Resilience4JRRetryProperties cbTaxIntegrationProperties() {
    return new Resilience4JRRetryProperties();
  }

  @Autowired
  @Bean
  public RetryRegistry retryRegistry(Resilience4JRRetryProperties cbTaxIntegrationProperties) {
    var mapOfConfigurations = Map.ofEntries(
        Map.entry(TAX_INTEGRATION_RETRY, this.getRetryConfiguration(cbTaxIntegrationProperties))
    );
    return RetryRegistry.of(mapOfConfigurations);
  }

  public RetryConfig getRetryConfiguration(Resilience4JRRetryProperties properties) {
    return RetryConfig.custom()
        .maxAttempts(properties.getMaxAttempts())
        .intervalFunction(IntervalFunction
            .ofExponentialBackoff(properties.getExponentialBackOffIntervalMillis(), properties.getExponentialBackOffMultiplier()))
        .build();
  }

}
