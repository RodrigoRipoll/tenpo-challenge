package ripoll.challenge.tenpoapi.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.model.PaymentTransaction;
import ripoll.challenge.tenpoapi.model.TheResponse;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;
import ripoll.challenge.tenpoapi.service.AccountantService;

@RestController
public class CalculateFinalAmount  {

    @Autowired
    private final AccountantService accountantService;

    @Autowired
    private final RequestLogRepository requestLogRepository;

    @Autowired
    public CalculateFinalAmount(AccountantService accountantService, RequestLogRepository requestLogRepository) {
        this.accountantService = accountantService;
        this.requestLogRepository = requestLogRepository;
    }

    @PostMapping("/accountant/transaction/final_value")
    public ResponseEntity<TheResponse> getTotalWithTaxes(@RequestBody PaymentTransaction paymentTransaction) throws Exception {
        return accountantService.getTotal(paymentTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
