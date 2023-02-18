package ripoll.challenge.tenpoapi.integration.tax;

import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCacheCache;

import java.time.Duration;
import java.util.Optional;

public class TaxIntegration {

    private final MemoryCacheCache<TaxCacheEntry> cache;
    private final TaxRestClient taxRestClient;
    private final Duration validityTimeOfTax = Duration.ofMinutes(30);

    public TaxIntegration(TaxRestClient taxRestClient, MemoryCacheCache<TaxCacheEntry> employeeCache) {
        this.taxRestClient = taxRestClient;
        this.cache = employeeCache;
    }

    public Double getCurrentTaxAsPercentage() {
        var cacheItem = cache.get("tax");

        return Optional.ofNullable(cacheItem)
                .filter(item -> !item.isExpired())
                .map(TaxCacheEntry::tax)
                .orElseGet(() -> {
                    Double askedTax = askForTaxValue();
                    if (askedTax == null && cacheItem == null) {
                        throw new RuntimeException("Tax not found");
                    }
                    if (askedTax != null) {
                        cache.add("tax", new TaxCacheEntry(askedTax, validityTimeOfTax));
                        return askedTax;
                    } else {
                        return cacheItem.tax();
                    }
                });
    }

    private Double askForTaxValue() {
        return Optional.ofNullable(taxRestClient.getCurrentTaxPercentage().getBody())
                .map(TaxPercentage::tax_percentage)
                .orElse(null);
    }

}

