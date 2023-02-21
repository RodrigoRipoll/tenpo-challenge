package ripoll.challenge.tenpoapi.ratelimiter;

import com.google.common.base.Preconditions;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.function.Supplier;

import static ripoll.challenge.tenpoapi.config.ratelimiter.RateLimitServiceConfig.DEFAULT_RATE_LIMIT;

public class RateLimitService {

    final ProxyManager<String> proxyManager;

    final Map<String, Supplier<BucketConfiguration>> bucketConfigurationRegistry;

    public RateLimitService(ProxyManager<String> proxyManager, Map<String, Supplier<BucketConfiguration>> bucketConfigurationRegistry) {
        this.proxyManager = proxyManager;
        this.bucketConfigurationRegistry = bucketConfigurationRegistry;
    }

    public Bucket resolveBucket(@NotNull String apiKey) {
        Preconditions.checkNotNull(apiKey, "api-key cannot be null at this point");
        var rateLimitKey = bucketConfigurationRegistry.containsKey(apiKey) ? apiKey : DEFAULT_RATE_LIMIT;
        return proxyManager.builder().build(rateLimitKey, bucketConfigurationRegistry.get(rateLimitKey));
    }
}
