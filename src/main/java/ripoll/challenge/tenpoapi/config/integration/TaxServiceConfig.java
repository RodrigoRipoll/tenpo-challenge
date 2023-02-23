package ripoll.challenge.tenpoapi.config.integration;

import static ripoll.challenge.tenpoapi.config.circuitbreaker.CircuitBreakerConfiguration.TAX_INTEGRATION_CB;
import static ripoll.challenge.tenpoapi.config.retry.RetryConfiguration.TAX_INTEGRATION_RETRY;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.integration.tax.TaxRestClient;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCache;

@Configuration
public class TaxServiceConfig {

    @Value("${integrations.base-url.tax}")
    private String taxIntegrationBaseUrl;

    @Bean
    public TaxRestClient taxRestClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(taxIntegrationBaseUrl)
                .build();
        WebClientAdapter webClientAdapter = WebClientAdapter.forClient(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(webClientAdapter).build();
        return factory.createClient(TaxRestClient.class);
    }

    @Bean
    @Autowired
    public TaxIntegration taxService(TaxRestClient taxRestClient,
                                     MemoryCache<TaxCacheEntry> taxCache,
                                     RetryRegistry retryRegistry,
                                     CircuitBreakerRegistry circuitBreakerRegistry) {
        return new TaxIntegration(
            taxRestClient,
            taxCache,
            retryRegistry.retry(TAX_INTEGRATION_RETRY, TAX_INTEGRATION_RETRY),
            circuitBreakerRegistry.circuitBreaker(TAX_INTEGRATION_CB, TAX_INTEGRATION_CB)
        );
    }
}
