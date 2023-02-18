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
public class FinalAmountController {

    private final AccountantService accountantService;

    @Autowired
    public FinalAmountController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("/accountant/transaction/final_value")
    public ResponseEntity<PaymentBrief> getTotalWithTaxes(@Valid @RequestBody PaymentTransaction paymentTransaction) {
        return accountantService.getPaymentBrief(paymentTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
