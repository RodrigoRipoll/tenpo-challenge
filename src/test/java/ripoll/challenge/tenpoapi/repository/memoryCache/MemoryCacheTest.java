package ripoll.challenge.tenpoapi.repository.memoryCache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.cache.Cache;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;

public class MemoryCacheTest {

  private MemoryCache<TaxCacheEntry> memoryCache;

  @Mock
  private Cache<String, TaxCacheEntry> cache;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.memoryCache = new MemoryCache<>(cache);
  }

  @Test
  public void testGet() {
    String key = "test-key";
    TaxCacheEntry expectedValue = new TaxCacheEntry(20.0, Duration.ofMinutes(5));
    when(cache.getIfPresent(key)).thenReturn(expectedValue);

    TaxCacheEntry actualValue = memoryCache.get(key);

    verify(cache).getIfPresent(key);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  public void testGetNotPresentKey() {
    String key = "test-key";
    when(cache.getIfPresent(key)).thenReturn(null);

    TaxCacheEntry actualValue = memoryCache.get(key);

    verify(cache).getIfPresent(key);
    assertNull(actualValue);
  }

  @Test
  public void testAdd() {
    String key = "test-key";
    TaxCacheEntry value = new TaxCacheEntry(20.0, Duration.ofMinutes(5));

    memoryCache.add(key, value);

    verify(cache).put(key, value);
  }
}