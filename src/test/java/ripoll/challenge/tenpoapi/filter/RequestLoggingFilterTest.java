package ripoll.challenge.tenpoapi.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLog;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RequestLoggingFilterTest {

  @Mock
  private IRequestLogRepository requestLogRepository;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Mock
  private ObjectMapper objectMapper;

  private RequestLoggingFilter requestLoggingFilter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    requestLoggingFilter = new RequestLoggingFilter(objectMapper, requestLogRepository);
  }

  @Test
  public void testDoFilterInternal() throws Exception {
    // given
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    String uri = "/test";
    when(request.getRequestURI()).thenReturn(uri);
    when(response.getStatus()).thenReturn(HttpStatus.OK.value());
    doNothing().when(filterChain).doFilter(eq(request), eq(responseWrapper));

    // when
    requestLoggingFilter.doFilterInternal(request, response, filterChain);

    // then
    verify(filterChain, times(1)).doFilter(eq(request), any(ContentCachingResponseWrapper.class));
    verify(requestLogRepository, times(1)).save(any(RequestLog.class));
  }

  @Test
  public void testGetRequestLogRepositoryIOException() throws JsonProcessingException {
    ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);
    String uri = "/test";
    when(request.getRequestURI()).thenReturn(uri);
    when(response.getStatus()).thenReturn(HttpStatus.OK.value());


    when(objectMapper.readTree(any(String.class))).thenThrow(new JsonParseException("m", null));

    ReflectionTestUtils.setField(requestLoggingFilter, "objectMapper", objectMapper);

    RequestLog requestLog = requestLoggingFilter.getRequestLogRepository(request, response, resp);
    assertNull(requestLog);
  }
}



