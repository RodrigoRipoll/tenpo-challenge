package ripoll.challenge.tenpoapi.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CircuitBreakerConfiguration {

    public static final String TAX_INTEGRATION_CB = "TAX-INTEGRATION-CB";

    @Bean
    @ConfigurationProperties("circuit-breaker.tax-integration")
    public Resilience4JCBProperties cbTaxIntegrationProperties() {
        return new Resilience4JCBProperties();
    }

    @Autowired
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(Resilience4JCBProperties cbTaxIntegrationProperties) {
        var mapOfConfigurations = Map.ofEntries(
                Map.entry(TAX_INTEGRATION_CB, this.getCbConfiguration(cbTaxIntegrationProperties))
        );
        return CircuitBreakerRegistry.of(mapOfConfigurations);
    }

    private CircuitBreakerConfig getCbConfiguration(Resilience4JCBProperties properties) {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(properties.getFailureRateThreshold())
            .waitDurationInOpenState(Duration.ofMillis(properties.getDurationInOpenStateInMillis()))
            .maxWaitDurationInHalfOpenState(Duration.ofMillis(properties.getDurationInHalfOpenStateInMillis()))
            .slowCallDurationThreshold(Duration.ofMillis(properties.getSlowCallDurationThresholdInMillis()))
            .slowCallRateThreshold(properties.getSlowCallRateThreshold())
            .permittedNumberOfCallsInHalfOpenState(properties.getPermittedNumberOfCallsInHalfOpenState())
            .slidingWindow(properties.getWindowSizeInSeconds(), properties.getMinNumberOfCalls(), CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
            .recordExceptions(IOException.class, TimeoutException.class)
            .ignoreExceptions(/*ExceptionToIgnore.class*/)
            .build();
    }
}

