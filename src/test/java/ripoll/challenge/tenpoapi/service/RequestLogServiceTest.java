package ripoll.challenge.tenpoapi.service;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ripoll.challenge.tenpoapi.repository.IRequestLogRepository;
import ripoll.challenge.tenpoapi.repository.RequestLog;

class RequestLogServiceTest {

  private RequestLogService requestLogService;

  @Mock
  private IRequestLogRepository requestLogRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    requestLogService = new RequestLogService(requestLogRepository);
  }

  @Test
  public void testGetAllLogs() {
    int page = 0;
    int size = 10;

    List<RequestLog> requestLogList = listOf(new RequestLog());
    Page<RequestLog> requestLogPage = new PageImpl<>(requestLogList);

    when(requestLogRepository.findAll(any(PageRequest.class))).thenReturn(requestLogPage);

    Page<RequestLog> resultPage = requestLogService.getAllLogs(page, size);

    assertEquals(requestLogPage, resultPage);
  }
}