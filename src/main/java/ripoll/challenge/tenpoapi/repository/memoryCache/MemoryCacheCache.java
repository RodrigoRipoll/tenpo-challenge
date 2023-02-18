package ripoll.challenge.tenpoapi.repository.memoryCache;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class MemoryCacheCache<T> implements IMemoryCache<T> {
    private final Cache<String, T> cache;

    public MemoryCacheCache(int expiryDuration, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void add(String key, T value) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(value);
        cache.put(key, value);
    }
}

