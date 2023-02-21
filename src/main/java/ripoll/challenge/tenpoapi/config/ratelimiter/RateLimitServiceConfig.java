package ripoll.challenge.tenpoapi.config.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ripoll.challenge.tenpoapi.config.cache.DistributedCacheConfig;
import ripoll.challenge.tenpoapi.ratelimiter.RateLimitService;

import javax.cache.CacheManager;
import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

@Configuration
@Import(DistributedCacheConfig.class)
public class RateLimitServiceConfig {

    public static final String RATE_LIMIT_CACHE_NAME = "AMAZING-RATE-LIMITER";

    public static final String DEFAULT_RATE_LIMIT = "DEFAULT-RATE-LIMIT";

    public static final String ALPHA_RATE_LIMIT = "ALPHA-RATE-LIMIT";

    public static final String BETA_RATE_LIMIT = "BETA-RATE-LIMIT";

    @Bean
    @Autowired
    public RateLimitService rateLimitService(ProxyManager<String> proxyManager,
                                             Map<String, Supplier<BucketConfiguration>> bucketConfigurationRegistry) {
        return new RateLimitService(proxyManager, bucketConfigurationRegistry);
    }

    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache(RATE_LIMIT_CACHE_NAME));
    }

    @Bean
    @Autowired
    public Map<String, Supplier<BucketConfiguration>> bucketConfigurationRegistry(RateLimitProperties defaultRateLimitProperties,
                                                                                  RateLimitProperties alphaRateLimitProperties,
                                                                                  RateLimitProperties betaRateLimitProperties) {

        return Map.ofEntries(
                Map.entry(DEFAULT_RATE_LIMIT, this.getConfigSupplier(defaultRateLimitProperties)),
                Map.entry(ALPHA_RATE_LIMIT, this.getConfigSupplier(alphaRateLimitProperties)),
                Map.entry(BETA_RATE_LIMIT, this.getConfigSupplier(betaRateLimitProperties))
                );
    }

    @Bean
    @ConfigurationProperties("rate-limit.default")
    public RateLimitProperties defaultRateLimitProperties() {
        return new RateLimitProperties();
    }

    @Bean
    @ConfigurationProperties("rate-limit.alpha")
    public RateLimitProperties alphaRateLimitProperties() {
        return new RateLimitProperties();
    }


    @Bean
    @ConfigurationProperties("rate-limit.beta")
    public RateLimitProperties betaRateLimitProperties() {
        return new RateLimitProperties();
    }

    private Supplier<BucketConfiguration> getConfigSupplier(RateLimitProperties properties) {
        Refill refill = Refill.intervally(properties.rpm, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(properties.rpm, refill);
        return () -> (BucketConfiguration.builder().addLimit(limit).build());
    }
}
