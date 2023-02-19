package ripoll.challenge.tenpoapi.ratelimiter;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RateLimitServiceTest {

  @Mock private ProxyManager<String> proxyManager;

  private RateLimitService rateLimitService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    rateLimitService = new RateLimitService(proxyManager);
  }

 /* @Test
  void resolveBucket_ShouldReturnBucket() {
    // given
    String key = "testKey";

    Refill refill = Refill.intervally(100, Duration.ofMinutes(1));
    Bandwidth limit = Bandwidth.classic(100, refill);

    LocalBucketBuilder localBucketBuilder = new LocalBucketBuilder();
    Bucket expectedBucket = localBucketBuilder.addLimit(limit).build();

    // mock ProxyManager to return expected bucket
    LocalBucketBuilder localBucketBuilderMock = new LocalBucketBuilder();
    Bucket bucket = localBucketBuilderMock.addLimit(limit).build();

    when(proxyManager.builder()).thenReturn((RemoteBucketBuilder<String>) localBucketBuilderMock);
    when(proxyManager.builder().build(eq(key), any(Supplier.class))).thenReturn((BucketProxy) bucket);

    // when
    Bucket actualBucket = rateLimitService.resolveBucket(key);

    // then
    assertEquals(expectedBucket.getAvailableTokens(), actualBucket.getAvailableTokens());
    assertEquals(99L, expectedBucket.tryConsumeAndReturnRemaining(1).getRemainingTokens());
  }*/
}
