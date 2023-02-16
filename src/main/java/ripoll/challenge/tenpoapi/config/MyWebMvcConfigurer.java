package ripoll.challenge.tenpoapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ripoll.challenge.tenpoapi.service.RateLimitInterceptor;
import ripoll.challenge.tenpoapi.service.RateLimitService;
import ripoll.challenge.tenpoapi.service.RequestTraceabilityInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private final RateLimitService rateLimitService;

    public MyWebMvcConfigurer(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitService));
        registry.addInterceptor(new RequestTraceabilityInterceptor());
    }
}