package ripoll.challenge.tenpoapi.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.model.TheResponse;
import ripoll.challenge.tenpoapi.service.AccountantService;

@RestController
public class CalculateFinalAmount {

    @Autowired
    private final AccountantService accountantService;

    public CalculateFinalAmount(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("/accountant/transaction/final_value")
    public ResponseEntity<TheResponse> getTotalWithTaxes(@RequestBody PaymentTransaction paymentTransaction) throws Exception {
        return accountantService.getTotal(paymentTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

/*    @PostMapping("/accountant/transaction/history")
    public ResponseEntity getTransactionsHistory() {
        return accountantService.getHistory()
                .map(ResponseEntity::ok)
                .orElse((ResponseEntity<TheResponse>) ResponseEntity.badRequest());
    }*/
}
