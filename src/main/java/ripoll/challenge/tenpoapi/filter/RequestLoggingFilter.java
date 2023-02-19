package ripoll.challenge.tenpoapi.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    private final ObjectMapper objectMapper;

    private final IRequestLogRepository IRequestLogRepository;

    public RequestLoggingFilter(ObjectMapper objectMapper, IRequestLogRepository IRequestLogRepository) {
        this.objectMapper = objectMapper;
        this.IRequestLogRepository = IRequestLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, resp);

        RequestLog requestLog = getRequestLogRepository(request, response, resp);

        resp.copyBodyToResponse();

        if (requestLog != null) this.saveAsync(requestLog);
    }

    RequestLog getRequestLogRepository(HttpServletRequest request, HttpServletResponse response, ContentCachingResponseWrapper resp) {
        try {
            byte[] responseBody = resp.getContentAsByteArray();
            String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
            JsonNode responseJson = objectMapper.readTree(responseBodyString);

            RequestLog requestLog = new RequestLog();
            requestLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            requestLog.setMethod(request.getMethod());
            requestLog.setUri(request.getRequestURI());
            requestLog.setStatusCode(response.getStatus());
            requestLog.setResponse(responseJson);
            return requestLog;
        } catch (IOException e) {
            LOGGER.error("Failed to parse response body: {}", e.getMessage());
            return null;
        }
    }

    @Async
    void saveAsync(RequestLog requestLog) {
        IRequestLogRepository.save(requestLog);
    }
}