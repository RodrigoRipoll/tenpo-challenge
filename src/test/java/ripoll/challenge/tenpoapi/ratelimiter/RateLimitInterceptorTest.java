package ripoll.challenge.tenpoapi.ratelimiter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateLimitInterceptorTest {
/*
  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Object handler;

  @Mock
  private RateLimitService rateLimitService;

  private RateLimitInterceptor rateLimitInterceptor;

  @BeforeEach
  void setUp() {
    rateLimitInterceptor = new RateLimitInterceptor(rateLimitService);
  }

  @Test
  void preHandle_withEnoughTokens_shouldReturnTrue() throws Exception {
    Bucket bucket = mock(Bucket.class);
    when(rateLimitService.resolveBucket(any())).thenReturn(bucket);
    ConsumptionProbe probe = mock(ConsumptionProbe.class);
    when(bucket.tryConsumeAndReturnRemaining(1L)).thenReturn(probe);
    when(probe.isConsumed()).thenReturn(true);
    when(probe.getRemainingTokens()).thenReturn(10L);

    boolean result = rateLimitInterceptor.preHandle(request, response, handler);

    assertTrue(result);
    verify(response).setHeader(eq("X-Rate-Limit-Remaining"), eq("10"));
    verify(rateLimitService).resolveBucket(eq("cache"));
    verify(bucket).tryConsumeAndReturnRemaining(1L);
    verify(probe).isConsumed();
    verify(probe).getRemainingTokens();
    verifyNoMoreInteractions(response, rateLimitService, bucket, probe);
  }

  @Test
  void preHandle_withNotEnoughTokens_shouldReturnFalseAndSetHeaders() throws Exception {
    Bucket bucket = mock(Bucket.class);
    when(rateLimitService.resolveBucket(any())).thenReturn(bucket);
    ConsumptionProbe probe = mock(ConsumptionProbe.class);
    when(bucket.tryConsumeAndReturnRemaining(1L)).thenReturn(probe);
    when(probe.isConsumed()).thenReturn(false);
    when(probe.getNanosToWaitForRefill()).thenReturn(TimeUnit.SECONDS.toNanos(10L));

    boolean result = rateLimitInterceptor.preHandle(request, response, handler);

    assertFalse(result);
    verify(response).setContentType(eq(MediaType.APPLICATION_JSON_VALUE));
    verify(response).setHeader(eq("X-Rate-Limit-Retry-After-Seconds"), eq("10"));
    verify(response).sendError(eq(HttpStatus.TOO_MANY_REQUESTS.value()), eq("You have exhausted your API Request Quota"));
    verify(rateLimitService).resolveBucket(eq("cache"));
    verify(bucket).tryConsumeAndReturnRemaining(eq(1L));
    verify(probe).isConsumed();
    verify(probe).getNanosToWaitForRefill();
    verifyNoMoreInteractions(response, rateLimitService, bucket, probe);
  }*/
}
