package ripoll.challenge.tenpoapi.E2E;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ripoll.challenge.tenpoapi.model.PaymentBrief;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.ratelimiter.RateLimitService;
import ripoll.challenge.tenpoapi.service.AccountantService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
class FinalAmountE2ETest {

  @MockBean
  private AccountantService accountantService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RateLimitService rateLimitService;

  @Test
  void getTotalWithTaxes_returnsOkResponse() throws Exception {
    // Arrange
    PaymentTransaction paymentTransaction = new PaymentTransaction(80.0, 20.0);
    PaymentBrief paymentBrief = new PaymentBrief(paymentTransaction, 10.0, 110.0);
    when(accountantService.getPaymentBrief(paymentTransaction)).thenReturn(Optional.of(paymentBrief));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/transaction/final_value")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"first_payment\": 80,  \"second_payment\": 20}");

    // Act and Assert
    mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payments.first_payment").value(80.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.payments.second_payment").value(20.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.payments.amount").value(100.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(10.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount").value(110.0));
  }

  @Test
  void getTotalWithTaxes_returnsBadRequestResponseWhenInvalidRequestBody() throws Exception {
    // Arrange
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/transaction/final_value")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"amount\": -1 }");

    // Act and Assert
    mockMvc.perform(request)
        .andExpect(status().isBadRequest());
  }
}

