package ripoll.challenge.tenpoapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLog;

public class RequestLogService {

    private final IRequestLogRepository IRequestLogRepository;

    public RequestLogService(IRequestLogRepository IRequestLogRepository) {
        this.IRequestLogRepository = IRequestLogRepository;
    }

    public Page<RequestLog> getAllLogs(Integer page, Integer size){
        return IRequestLogRepository.findAll(PageRequest.of(page, size));
    }
}
