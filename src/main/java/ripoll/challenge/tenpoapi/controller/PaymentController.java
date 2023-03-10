package ripoll.challenge.tenpoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.model.PaymentBrief;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.service.AccountantService;

import jakarta.validation.Valid;

@RestController
public class PaymentController {

    public static final String ENDPOINT_PAYMENTS_BRIEF = "/accountant/payments/brief";

    private final AccountantService accountantService;

    @Autowired
    public PaymentController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping(ENDPOINT_PAYMENTS_BRIEF)
    public ResponseEntity<PaymentBrief> getTotalWithTaxes(@Valid @RequestBody PaymentTransaction paymentTransaction) {
        return accountantService.getPaymentBrief(paymentTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
