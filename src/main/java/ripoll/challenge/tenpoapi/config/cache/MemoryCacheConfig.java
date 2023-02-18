package ripoll.challenge.tenpoapi.config.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCacheCache;

import java.util.concurrent.TimeUnit;

@Configuration
public class MemoryCacheConfig {

    @Bean
    public MemoryCacheCache<TaxCacheEntry> employeeCache() {
        return new MemoryCacheCache<TaxCacheEntry>(120, TimeUnit.SECONDS);
    }
}