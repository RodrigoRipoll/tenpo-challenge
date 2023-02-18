package ripoll.challenge.tenpoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.model.PaymentBrief;

import java.util.Optional;

@Service
public class AccountantService {

    @Autowired
    private final TaxIntegration taxIntegration;

    public AccountantService(TaxIntegration taxIntegration) {
        this.taxIntegration = taxIntegration;
    }

    public Optional<PaymentBrief> getPaymentBrief(PaymentTransaction paymentTransaction) {
        final Double taxesPercentage = taxIntegration.getCurrentTaxAsPercentage();
        final double totalPaymentsBeforeTaxes = paymentTransaction.getTotalPayments();
        final Double totalWithTaxes = totalPaymentsBeforeTaxes + totalPaymentsBeforeTaxes * (taxesPercentage/100);

        return Optional.of(new PaymentBrief(paymentTransaction, taxesPercentage, totalWithTaxes));
    }
}
