package ripoll.challenge.tenpoapi.config.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.integration.tax.TaxRestClient;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCacheCache;

@Configuration
public class TaxServiceConfig {
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



    @Bean
    @Autowired
    public TaxIntegration taxService(TaxRestClient taxRestClient, MemoryCacheCache<TaxCacheEntry> employeeCache) {
        return new TaxIntegration(taxRestClient, employeeCache);
    }
}
