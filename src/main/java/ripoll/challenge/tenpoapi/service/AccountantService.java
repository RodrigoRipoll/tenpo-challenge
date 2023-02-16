package ripoll.challenge.tenpoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ripoll.challenge.tenpoapi.integrations.TaxService;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.model.TheResponse;

import java.util.Optional;

@Service
public class AccountantService {

    @Autowired
    private final TaxService taxService;

    public AccountantService(TaxService taxService) {
        this.taxService = taxService;
    }

    public Optional<TheResponse> getTotal(PaymentTransaction paymentTransaction) throws Exception {
        final Double taxesPercentage = taxService.getCurrentTaxAsPercentage();
        final double totalPaymentsBeforeTaxes = paymentTransaction.getTotalPayments();
        final Double totalWithTaxes = totalPaymentsBeforeTaxes + totalPaymentsBeforeTaxes * (taxesPercentage/100);

        return Optional.of(new TheResponse(paymentTransaction, taxesPercentage, totalWithTaxes));
    }
}
