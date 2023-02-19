package ripoll.challenge.tenpoapi.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ripoll.challenge.tenpoapi.service.AccountantService;

@SpringBootTest
class FinalAmountControllerTest {

  @Mock
  private AccountantService accountantService;

  @InjectMocks
  private FinalAmountController controller;
/*
  @Test
  void getTotalWithTaxes_returnsOkResponse() {
    // Arrange
    PaymentTransaction paymentTransaction = new PaymentTransaction(5.0, 5.0);
    PaymentBrief paymentBrief = new PaymentBrief(paymentTransaction, 10.0, 110.0);
    when(accountantService.getPaymentBrief(paymentTransaction)).thenReturn(Optional.of(paymentBrief));

    // Act
    ResponseEntity<PaymentBrief> response = controller.getTotalWithTaxes(paymentTransaction);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(paymentBrief);
  }

  @Test
  void getTotalWithTaxes_returnsBadRequestResponse() {
    // Arrange
    PaymentTransaction paymentTransaction = new PaymentTransaction(5.0, 5.0);
    when(accountantService.getPaymentBrief(paymentTransaction)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<PaymentBrief> response = controller.getTotalWithTaxes(paymentTransaction);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNull();
  }*/
}
