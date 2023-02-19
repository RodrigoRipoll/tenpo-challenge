package ripoll.challenge.tenpoapi.controller;

import jakarta.validation.constraints.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.service.RequestLogService;

import jakarta.validation.constraints.Min;
import java.util.Optional;

@RestController
@Validated
public class RequestLogController {

    private final static String VALIDATION_SIZE_MESSAGE = "This is an optional field. If used, it cannot be less than 1 or greater than 20.";
    private final RequestLogService requestLogService;

    @Autowired
    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @GetMapping("/accountant/request_log/history")
    public ResponseEntity<Page<RequestLog>> getRequestLogHistory(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5")
            @Min(value = 1 , message = VALIDATION_SIZE_MESSAGE)
            @Max(value = 20 , message = VALIDATION_SIZE_MESSAGE)
            Integer size) {
        return Optional.of(requestLogService.getAllLogs(page, size))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
