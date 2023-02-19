package ripoll.challenge.tenpoapi.repository.memoryCache;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;

public class MemoryCache<T> implements IMemoryCache<T> {
    private final Cache<String, T> cache;

    public MemoryCache(Cache<String, T> cache) {
        this.cache = cache;
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

