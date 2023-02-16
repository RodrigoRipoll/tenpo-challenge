package ripoll.challenge.tenpoapi.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;

@Component
public class RequestTraceabilityInterceptor implements HandlerInterceptor {

    @Autowired
    private RequestLogRepository requestLogRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Este método se ejecuta después de que se maneje la solicitud en el controlador

        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        String responseBody = responseWrapper.getResponseBody();

        RequestLog requestLog = new RequestLog();
        requestLog.setMethod(request.getMethod());
        requestLog.setUri(request.getRequestURI());
        requestLog.setStatusCode(response.getStatus());
        requestLog.setResponseBody(responseBody);

        requestLogRepository.save(requestLog);
    }
}