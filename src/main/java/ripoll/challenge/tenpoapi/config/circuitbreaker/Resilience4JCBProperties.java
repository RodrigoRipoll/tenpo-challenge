package ripoll.challenge.tenpoapi.config.circuitbreaker;

import lombok.Data;

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

}
