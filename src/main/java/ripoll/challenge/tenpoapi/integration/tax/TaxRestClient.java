package ripoll.challenge.tenpoapi.integration.tax;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;

public interface TaxRestClient {
    @GetExchange("/tax/payments/current")
    ResponseEntity<TaxPercentage> getCurrentTaxPercentage();
}