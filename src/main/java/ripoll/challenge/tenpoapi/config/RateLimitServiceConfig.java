package ripoll.challenge.tenpoapi.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.ratelimiter.RateLimitService;

@Configuration
public class RateLimitServiceConfig {

  @Bean
  @Autowired
  public RateLimitService rateLimitService(ProxyManager<String> proxyManager) {
    return new RateLimitService(proxyManager);
  }
}
