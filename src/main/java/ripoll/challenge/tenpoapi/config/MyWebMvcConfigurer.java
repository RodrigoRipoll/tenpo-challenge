package ripoll.challenge.tenpoapi.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;
import ripoll.challenge.tenpoapi.service.MyFilter;
import ripoll.challenge.tenpoapi.service.RateLimitInterceptor;
import ripoll.challenge.tenpoapi.service.RateLimitService;
import ripoll.challenge.tenpoapi.service.RequestTraceabilityInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private final RateLimitService rateLimitService;

    @Autowired
    private final RequestLogRepository requestLogRepository;

    public MyWebMvcConfigurer(RateLimitService rateLimitService, RequestLogRepository requestLogRepository) {
        this.rateLimitService = rateLimitService;
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitService));
        registry.addInterceptor(new RequestTraceabilityInterceptor(requestLogRepository));
    }

    @Bean
    public FilterRegistrationBean<MyFilter> loggingFilter(RequestLogRepository requestLogRepository){
        FilterRegistrationBean<MyFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new MyFilter(requestLogRepository));

        return registrationBean;
    }
}