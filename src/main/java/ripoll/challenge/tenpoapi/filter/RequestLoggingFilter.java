package ripoll.challenge.tenpoapi.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@Component
@Order(-999)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final IRequestLogRepository IRequestLogRepository;

    public RequestLoggingFilter(IRequestLogRepository IRequestLogRepository) {
        this.IRequestLogRepository = IRequestLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, resp);

        RequestLogRepository requestLogRepository = getRequestLogRepository(request, response, resp);

        resp.copyBodyToResponse();
        this.saveWithDelay(requestLogRepository);
    }

    private static RequestLogRepository getRequestLogRepository(HttpServletRequest request, HttpServletResponse response, ContentCachingResponseWrapper resp) throws IOException {
        byte[] responseBody = resp.getContentAsByteArray();
        String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBodyString);

        RequestLogRepository requestLogRepository = new RequestLogRepository();
        requestLogRepository.setTimestamp(new Timestamp(System.currentTimeMillis()));
        requestLogRepository.setMethod(request.getMethod());
        requestLogRepository.setUri(request.getRequestURI());
        requestLogRepository.setStatusCode(response.getStatus());
        requestLogRepository.setResponse(responseJson);
        return requestLogRepository;
    }

    //@Async
    void saveWithDelay(RequestLogRepository requestLogRepository){
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        IRequestLogRepository.save(requestLogRepository);
    }
}