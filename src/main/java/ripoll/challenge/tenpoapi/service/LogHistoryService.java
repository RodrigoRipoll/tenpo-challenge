package ripoll.challenge.tenpoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ripoll.challenge.tenpoapi.repository.RequestLog;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;

@Service
public class LogHistoryService {
    @Autowired
    private final RequestLogRepository requestLogRepository;

    public LogHistoryService(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    public Page<RequestLog> getLogHistory (Integer page, Integer size){
        return requestLogRepository.findAll(PageRequest.of(page, size));
    }
}
