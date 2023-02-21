package ripoll.challenge.tenpoapi.ratelimiter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";
    private static final String HEADER_API_KEY = "X-Api-Key";
    private static final String ERROR_MESSAGE_TOO_MANY_REQUESTS = "You have exhausted your API Request Quota";
    private final RateLimitService rateLimitService;
    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Bucket bucket = getBucket(request);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.setHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long secondsToWaitForRefill = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HEADER_RETRY_AFTER, String.valueOf(secondsToWaitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), ERROR_MESSAGE_TOO_MANY_REQUESTS);
            return false;
        }
    }

    private Bucket getBucket(HttpServletRequest request) {
        String apiKey = Optional.ofNullable(request.getHeader(HEADER_API_KEY))
                .map(header -> header.toUpperCase(Locale.ROOT))
                .orElse("");
        return rateLimitService.resolveBucket(apiKey);
    }
}
