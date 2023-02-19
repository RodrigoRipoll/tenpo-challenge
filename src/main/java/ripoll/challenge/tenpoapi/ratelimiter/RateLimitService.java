package ripoll.challenge.tenpoapi.ratelimiter;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class RateLimitService {

    final ProxyManager<String> proxyManager;
    @Autowired
    public RateLimitService(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplier();
        return proxyManager.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplier() {
        Refill refill = Refill.intervally(100, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(100, refill);
        return () -> (BucketConfiguration.builder().addLimit(limit).build());

    }
}
