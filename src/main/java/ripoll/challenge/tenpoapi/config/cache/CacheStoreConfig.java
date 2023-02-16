package ripoll.challenge.tenpoapi.config.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.integrations.CacheItem;
import ripoll.challenge.tenpoapi.repository.cache.CacheStore;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStoreConfig<C> {

    @Bean
    public CacheStore<CacheItem> employeeCache() {
        return new CacheStore<CacheItem>(120, TimeUnit.SECONDS);
    }

}