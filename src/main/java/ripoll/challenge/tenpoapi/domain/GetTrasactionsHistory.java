package ripoll.challenge.tenpoapi.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;
import ripoll.challenge.tenpoapi.service.LogHistoryService;

import java.util.List;
import java.util.Optional;

@RestController
public class GetTrasactionsHistory {

    @Autowired
    private final RequestLogRepository requestLogRepository;

    @Autowired
    private final LogHistoryService logHistoryService;

    public GetTrasactionsHistory(RequestLogRepository requestLogRepository, LogHistoryService logHistoryService) {
        this.requestLogRepository = requestLogRepository;
        this.logHistoryService = logHistoryService;
    }

    @GetMapping("/accountant/transaction/history")
    public ResponseEntity<Page<RequestLog>> getTransactionsHistory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "3") Integer size
    ) {
        return Optional.of(logHistoryService.getLogHistory(page,size))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
