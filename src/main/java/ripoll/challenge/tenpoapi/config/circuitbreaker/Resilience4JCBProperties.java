package ripoll.challenge.tenpoapi.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.Data;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Data
public class Resilience4JCBProperties {

    private Integer failureRateThreshold;
    private Integer durationInOpenStateInMillis;
    private Integer durationInHalfOpenStateInMillis;
    private Integer permittedNumberOfCallsInHalfOpenState;
    private Integer minNumberOfCalls;
    private Integer windowSizeInSeconds;
    private Integer slowCallDurationThresholdInMillis;
    private Integer slowCallRateThreshold;

    public CircuitBreakerConfig getCbConfiguration() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofMillis(durationInOpenStateInMillis))
                .maxWaitDurationInHalfOpenState(Duration.ofMillis(durationInHalfOpenStateInMillis))
                .slowCallDurationThreshold(Duration.ofMillis(slowCallDurationThresholdInMillis))
                .slowCallRateThreshold(slowCallRateThreshold)
                .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
                .slidingWindow(windowSizeInSeconds, minNumberOfCalls, CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .recordExceptions(IOException.class, TimeoutException.class)
                .ignoreExceptions(/*CannotUpdateItemException.class*/)
                .build();
    }

    public CircuitBreakerConfig getCBConfigurationDefault() {
        return CircuitBreakerConfig.ofDefaults();
    }
}
