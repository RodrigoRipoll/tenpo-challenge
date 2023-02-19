package ripoll.challenge.tenpoapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ripoll.challenge.tenpoapi.integration.tax.TaxIntegration;
import ripoll.challenge.tenpoapi.model.PaymentBrief;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;

class AccountantServiceTest {

  private AccountantService accountantService;

  @Mock
  private TaxIntegration taxIntegration;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    accountantService = new AccountantService(taxIntegration);
  }

  @Test
  public void testGetPaymentBrief() {
    PaymentTransaction paymentTransaction = new PaymentTransaction(20.0, 80.0);

    when(taxIntegration.getCurrentTaxAsPercentage()).thenReturn(15.0);

    Optional<PaymentBrief> response = accountantService.getPaymentBrief(paymentTransaction);
    assertTrue(response.isPresent());

    PaymentBrief paymentBrief = response.get();
    assertEquals(20.0, paymentBrief.payments().firstTxAmount());
    assertEquals(80.0, paymentBrief.payments().secondTxAmount());
    assertEquals(100.0, paymentBrief.payments().getTotalPayments());
    assertEquals(15.0, paymentBrief.taxes());
    assertEquals(115.0, paymentBrief.totalAmount());
  }

  @Test
  public void testCalculateTotalWithTaxes() {
    double totalPayments = 100.0;
    double taxesPercentage = 20.0;

    double totalWithTaxes = accountantService.calculateTotalWithTaxes(totalPayments, taxesPercentage);

    assertEquals(120.0, totalWithTaxes);
  }

  @Test
  public void testCalculateTotalWithNoTaxes() {
    double totalPayments = 100.0;
    double taxesPercentage = 0.0;

    double totalWithTaxes = accountantService.calculateTotalWithTaxes(totalPayments, taxesPercentage);

    assertEquals(100.0, totalWithTaxes);
  }

  @Test
  public void testCalculateTotalWithMinusTaxes() {
    double totalPayments = 100.0;
    double taxesPercentage = -20.0;

    double totalWithTaxes = accountantService.calculateTotalWithTaxes(totalPayments, taxesPercentage);

    assertEquals(80.0, totalWithTaxes);
  }
}