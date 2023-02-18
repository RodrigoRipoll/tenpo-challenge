package ripoll.challenge.tenpoapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.filter.RequestLoggingFilter;
import ripoll.challenge.tenpoapi.ratelimiter.RateLimitInterceptor;
import ripoll.challenge.tenpoapi.ratelimiter.RateLimitService;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private final RateLimitService rateLimitService;

    public WebConfigurer(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitService));
    }

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter(IRequestLogRepository IRequestLogRepository) {
        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLoggingFilter(IRequestLogRepository));
        return registrationBean;
    }
}