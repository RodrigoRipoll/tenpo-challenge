package ripoll.challenge.tenpoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.model.PaymentBrief;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;

import java.util.Optional;

@Service
public class AccountantService {

    private final TaxIntegration taxIntegration;

    @Autowired
    public AccountantService(TaxIntegration taxIntegration) {
        this.taxIntegration = taxIntegration;
    }

    public Optional<PaymentBrief> getPaymentBrief(PaymentTransaction paymentTransaction) {
        final double taxesPercentage = taxIntegration.getCurrentTaxAsPercentage();
        final double totalPayments = paymentTransaction.getTotalPayments();
        final double totalWithTaxes = calculateTotalWithTaxes(totalPayments, taxesPercentage);

        return Optional.of(new PaymentBrief(paymentTransaction, taxesPercentage, totalWithTaxes));
    }

    double calculateTotalWithTaxes(double totalPayments, double taxesPercentage) {
        return totalPayments + totalPayments * (taxesPercentage / 100);
    }
}
