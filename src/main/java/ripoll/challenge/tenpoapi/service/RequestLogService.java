package ripoll.challenge.tenpoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ripoll.challenge.tenpoapi.repository.RequestLogRepository;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;

@Service
public class RequestLogService {
    @Autowired
    private final IRequestLogRepository IRequestLogRepository;

    public RequestLogService(IRequestLogRepository IRequestLogRepository) {
        this.IRequestLogRepository = IRequestLogRepository;
    }

    public Page<RequestLogRepository> getAllLogs(Integer page, Integer size){
        return IRequestLogRepository.findAll(PageRequest.of(page, size));
    }
}
