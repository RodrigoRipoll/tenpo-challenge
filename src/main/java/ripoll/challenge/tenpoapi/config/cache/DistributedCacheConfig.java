package ripoll.challenge.tenpoapi.config.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;

import static ripoll.challenge.tenpoapi.config.ratelimiter.RateLimitServiceConfig.RATE_LIMIT_CACHE_NAME;

@Configuration
public class DistributedCacheConfig {

    @Value("${redisUrlConnection}")
    private String redisUrlConnection;
    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(getRedissonConfig());
    }
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache(RATE_LIMIT_CACHE_NAME, RedissonConfiguration.fromConfig(redissonClient.getConfig()));
        return manager;
    }

    private Config getRedissonConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrlConnection);
        return config;
    }
}