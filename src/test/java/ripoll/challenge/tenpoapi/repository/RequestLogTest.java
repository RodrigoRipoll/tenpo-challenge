package ripoll.challenge.tenpoapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class RequestLogTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testRequestLog() {
    Long id = 1L;
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    String method = "GET";
    String uri = "/api/test";
    int statusCode = 200;
    String responseBody = "{\"message\":\"success\"}";
    JsonNode response = objectMapper.convertValue(responseBody, JsonNode.class);

    RequestLog requestLog = new RequestLog();
    requestLog.setId(id);
    requestLog.setTimestamp(timestamp);
    requestLog.setMethod(method);
    requestLog.setUri(uri);
    requestLog.setStatusCode(statusCode);
    requestLog.setResponse(response);

    assertThat(requestLog.getId()).isEqualTo(id);
    assertThat(requestLog.getTimestamp()).isEqualTo(timestamp);
    assertThat(requestLog.getMethod()).isEqualTo(method);
    assertThat(requestLog.getUri()).isEqualTo(uri);
    assertThat(requestLog.getStatusCode()).isEqualTo(statusCode);
    assertThat(requestLog.getResponse()).isEqualTo(response);
  }
}




