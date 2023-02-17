package ripoll.challenge.tenpoapi.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class RequestTraceabilityInterceptor implements HandlerInterceptor {

    private static Logger log = LoggerFactory.getLogger(RequestTraceabilityInterceptor.class);
    private final RequestLogRepository requestLogRepository;

    public RequestTraceabilityInterceptor(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

   /* @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Este método se ejecuta después de que se maneje la solicitud en el controlador

        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        // Get Cache
        byte[] responseBody = resp.getContentAsByteArray();
        String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);


        log.info("response body afterCompletion = {}", responseBodyString);

        // Finally remember to respond to the client with the cached data.
        resp.setContentType(response.getContentType());
        resp.copyBodyToResponse();

        RequestLog requestLog = new RequestLog();
        requestLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        requestLog.setMethod(request.getMethod());
        requestLog.setUri(request.getRequestURI());
        requestLog.setStatusCode(response.getStatus());
        requestLog.setResponse(responseBodyString);

        requestLogRepository.save(requestLog);
    }*/

}