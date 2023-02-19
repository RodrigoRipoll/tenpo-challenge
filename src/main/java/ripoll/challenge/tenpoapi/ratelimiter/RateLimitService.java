package ripoll.challenge.tenpoapi.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.function.Supplier;

public class RateLimitService {

    @Value("${rate-limit.rpm}")
    private long rpmAllowed;

    final ProxyManager<String> proxyManager;

    public RateLimitService(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplier();
        return proxyManager.builder().build(key, configSupplier);
    }

    private Supplier<BucketConfiguration> getConfigSupplier() {
        Refill refill = Refill.intervally(rpmAllowed, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(rpmAllowed, refill);
        return () -> (BucketConfiguration.builder().addLimit(limit).build());
    }
}
