package ripoll.challenge.tenpoapi.config.ratelimiter;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;

@Configuration
public class DistributedCacheConfig {

    @Value("${redis_url_connection}")
    private String redisUrlConnection;
    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(getRedissonConfig());
    }
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        manager.createCache("cache", RedissonConfiguration.fromConfig(redissonClient.getConfig()));
        return manager;
    }

    @Bean
    ProxyManager<String> proxyManager(CacheManager cacheManager) {
        return new JCacheProxyManager<>(cacheManager.getCache("cache"));
    }

    private Config getRedissonConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrlConnection);
        return config;
    }
}