package ripoll.challenge.tenpoapi.config.business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.service.AccountantService;

@Configuration
public class AccountServiceConfig {

  @Bean
  @Autowired
  public AccountantService accountantService(TaxIntegration taxIntegration) {
    return new AccountantService(taxIntegration);
  }

}
