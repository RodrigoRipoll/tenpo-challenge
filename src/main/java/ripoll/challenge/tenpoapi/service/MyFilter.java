package ripoll.challenge.tenpoapi.service;

import ch.qos.logback.core.util.ContentTypeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;

@Component
@Order(-999)
public class MyFilter extends OncePerRequestFilter {

    private static Logger log = LoggerFactory.getLogger(RequestTraceabilityInterceptor.class);
    private final RequestLogRepository requestLogRepository;

    public MyFilter(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);


        // Execution request chain
        filterChain.doFilter(request, resp);

        // Get Cache
        byte[] responseBody = resp.getContentAsByteArray();
        String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);


        log.info("response body = {}", responseBodyString);

        // Finally remember to respond to the client with the cached data.
        resp.setContentType(response.getContentType());
        resp.copyBodyToResponse();

        RequestLog requestLog = new RequestLog();
        requestLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        requestLog.setMethod(request.getMethod());
        requestLog.setUri(request.getRequestURI());
        requestLog.setStatusCode(response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(responseBodyString);

        requestLog.setResponse(responseJson);

        this.saveWithDelay(requestLog);
        System.out.println("doFilterInternal exit {}"+ new Timestamp(System.currentTimeMillis()));

    }

   // @Async
    void saveWithDelay(RequestLog requestLog){
//        System.out.println("saveWithDelay {}" + new Timestamp(System.currentTimeMillis()));
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("saveWithDelay requestLogRepository.save {}"+ new Timestamp(System.currentTimeMillis()));
        requestLogRepository.save(requestLog);
    }
}