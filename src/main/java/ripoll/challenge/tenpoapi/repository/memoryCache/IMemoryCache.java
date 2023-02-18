package ripoll.challenge.tenpoapi.repository.memoryCache;

public interface IMemoryCache<T> {
    T get(String key);
    void add(String key, T value);
}