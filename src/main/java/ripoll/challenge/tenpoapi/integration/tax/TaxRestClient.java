package ripoll.challenge.tenpoapi.integration.tax;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;

public interface TaxRestClient {
    @GetExchange("/tax/payments/current")
    @CircuitBreaker(name = "myCircuitBreaker")
    @Retry(name = "myRetry")
    ResponseEntity<TaxPercentage> getCurrentTaxPercentage();
}