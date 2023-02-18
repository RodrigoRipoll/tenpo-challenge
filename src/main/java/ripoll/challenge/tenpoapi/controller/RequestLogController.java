package ripoll.challenge.tenpoapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;
import ripoll.challenge.tenpoapi.service.RequestLogService;

import java.util.Optional;

@RestController
public class RequestLogController {

    @Autowired
    private final RequestLogService requestLogService;

    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @GetMapping("/accountant/transaction/history")
    public ResponseEntity<Page<RequestLogRepository>> getTransactionsHistory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "3") Integer size) {
        return Optional.of(requestLogService.getAllLogs(page, size))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
