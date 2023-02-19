package ripoll.challenge.tenpoapi.config.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.service.RequestLogService;

@Configuration
public class RequestLogServiceConfig {

  @Bean
  @Autowired
  public RequestLogService accountantService(IRequestLogRepository IRequestLogRepository) {
    return new RequestLogService(IRequestLogRepository);
  }
}
