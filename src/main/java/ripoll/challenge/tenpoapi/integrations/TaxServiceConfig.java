package ripoll.challenge.tenpoapi.integrations;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ripoll.challenge.tenpoapi.repository.cache.CacheStore;

@Configuration
public class TaxServiceConfig {

    interface TaxRestClient {
        @GetExchange("/tax/payments/current")
        @CircuitBreaker(name = "myCircuitBreaker")
        @Retry(name = "myRetry")
        ResponseEntity<TaxPercentage> getCurrentTaxPercentage();
    }

    @Bean
    public TaxRestClient taxRestClient() {
        WebClient webClient = WebClient.builder()
                //.baseUrl("tenpo-tax-api")
                .baseUrl("http://localhost:8081")
                .build();
        WebClientAdapter webClientAdapter = WebClientAdapter.forClient(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(webClientAdapter)
                .build();
        return factory.createClient(TaxRestClient.class);
    }

    record TaxPercentage(Double tax_percentage) {
    }

    @Bean
    @Autowired
    public TaxService taxService(TaxRestClient taxRestClient, CacheStore<CacheItem> employeeCache) {
        return new TaxService(taxRestClient, employeeCache);
    }
}
