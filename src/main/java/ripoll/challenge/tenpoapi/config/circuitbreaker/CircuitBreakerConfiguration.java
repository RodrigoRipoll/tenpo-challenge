package ripoll.challenge.tenpoapi.config.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
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
                Map.entry(TAX_INTEGRATION_CB, cbTaxIntegrationProperties.getCbConfiguration())
        );
        return CircuitBreakerRegistry.of(mapOfConfigurations);
    }
}

