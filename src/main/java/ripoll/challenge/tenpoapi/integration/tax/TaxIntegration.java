package ripoll.challenge.tenpoapi.integration.tax;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import ripoll.challenge.tenpoapi.exception.CannotRetrieveTaxException;
import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCache;

import java.time.Duration;
import java.util.Optional;

public class TaxIntegration {

    public static final String TAX_CACHE_KEY = "tax";
    private final MemoryCache<TaxCacheEntry> cache;
    private final TaxRestClient taxRestClient;

    @Value("${tax.expire}")
    private final Duration timeToExpireTax = Duration.ofMinutes(30);

    public TaxIntegration(TaxRestClient taxRestClient, MemoryCache<TaxCacheEntry> taxCache) {
        this.taxRestClient = taxRestClient;
        this.cache = taxCache;
    }

    public double getCurrentTaxAsPercentage() {
        TaxCacheEntry taxCacheEntry = cache.get(TAX_CACHE_KEY);

        return Optional.ofNullable(taxCacheEntry)
                .filter(item -> !item.isExpired())
                .map(TaxCacheEntry::tax)
                .orElseGet(()-> retrieveTaxFromCacheOrServer(taxCacheEntry));
    }

    private double retrieveTaxFromCacheOrServer(TaxCacheEntry fallback) {
        Double serverTax = askForTaxValue();
        if (serverTax == null && fallback == null) {
            throw new CannotRetrieveTaxException();
        }
        if (serverTax != null) {
            cache.add(TAX_CACHE_KEY, new TaxCacheEntry(serverTax, timeToExpireTax));
            return serverTax;
        } else {
            return fallback.tax();
        }
    }

    private Double askForTaxValue() {
        try {
            return Optional.ofNullable(taxRestClient.getCurrentTaxPercentage())
                    .map(HttpEntity::getBody)
                    .map(TaxPercentage::tax_percentage)
                    .orElse(null);
        } catch (Exception e) {
            return  null;
        }
    }
}

