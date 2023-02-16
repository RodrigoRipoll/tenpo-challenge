package ripoll.challenge.tenpoapi.integrations;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import ripoll.challenge.tenpoapi.repository.cache.CacheStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaxService {

    private final CacheStore<CacheItem> cache;
    private TaxServiceConfig.TaxRestClient taxRestClient;

    private Duration validityTimeOfTax = Duration.ofMinutes(30);

    public TaxService(TaxServiceConfig.TaxRestClient taxRestClient, CacheStore<CacheItem> employeeCache) {
        this.taxRestClient = taxRestClient;
        this.cache = employeeCache;
    }

    public Double getCurrentTaxAsPercentage() throws Exception {
        var cacheItem = cache.get("tax");

        return Optional.ofNullable(cacheItem)
                .filter(item -> !item.isExpired())
                .map(CacheItem::tax)
                .orElseGet(() -> {
                    Double askedTax = askForTaxValue();
                    if (askedTax == null && cacheItem == null) {
                        throw new RuntimeException("Tax not found");
                    }
                    if (askedTax != null) {
                        cache.add("tax", new CacheItem(askedTax, validityTimeOfTax));
                        return askedTax;
                    } else {
                        return cacheItem.tax();
                    }
                });
    }



    private Double askForTaxValue() {
        System.out.println(" Making a request to at :" + LocalDateTime.now());
        return Optional.ofNullable(taxRestClient.getCurrentTaxPercentage().getBody())
                .map(TaxServiceConfig.TaxPercentage::tax_percentage)
                .orElse(null);
    }

}

