package ripoll.challenge.tenpoapi.config.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCache;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MemoryCacheConfig {

    @Value("${cache.duration}")
    private Duration cacheDuration;

    @Bean
    public Cache<String, TaxCacheEntry> cacheTax() {
        return CacheBuilder.newBuilder()
            .expireAfterWrite(cacheDuration.getSeconds(), TimeUnit.SECONDS)
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build();
    }

    @Bean
    public MemoryCache<TaxCacheEntry> memoryTaxCache(Cache<String, TaxCacheEntry> cacheTax) {
        return new MemoryCache<>(cacheTax);
    }
}