/*
package ripoll.challenge.tenpoapi.controller;


import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ripoll.challenge.tenpoapi.repository.RequestLog;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ripoll.challenge.tenpoapi.controller.MockServiceMother.objectMapper;
import static ripoll.challenge.tenpoapi.controller.RequestLogController.ENDPOINT_REQUEST_LOG_HISTORY;

class RequestLogE2ETest extends IntegrationTest {

    @Test
    void getRequestLogHistory_returnsOkResponse() throws Exception {
        // Arrange
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        String method = "GET";
        String uri = "/api/test";
        int statusCode = 200;
        String responseBody = "{\"message\":\"success\"}";
        JsonNode response = objectMapper.convertValue(responseBody, JsonNode.class);

        RequestLog requestLog = new RequestLog();
        requestLog.setTimestamp(timestamp);
        requestLog.setMethod(method);
        requestLog.setUri(uri);
        requestLog.setStatusCode(statusCode);
        requestLog.setResponse(response);

        requestLogRepository.save(requestLog);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ENDPOINT_REQUEST_LOG_HISTORY);

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].method").value(method))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].status_code").value(statusCode))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].response").value(response));
    }

    @Test
    void getRequestLogHistory_returnsBadRequestResponseWhenInvalidQueryParamSize() throws Exception {
        // Arrange
        String path = String.format("%s%s", ENDPOINT_REQUEST_LOG_HISTORY, "?size=22");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(path);

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestLogHistory_returnsBadRequestResponseWhenInvalidQueryParamPage() throws Exception {
        // Arrange
        String path = String.format("%s%s", ENDPOINT_REQUEST_LOG_HISTORY, "?page=-1");
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(path);

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTotalWithTaxes_returnsBadRequestResponseWhenRepositoryFail() throws Exception {
        // Arrange
        var exposedPorts = postgreSQLContainer.getExposedPorts();
        postgreSQLContainer.setExposedPorts(List.of()); // try to force an Error
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ENDPOINT_REQUEST_LOG_HISTORY);

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
        // re-Arrange
        postgreSQLContainer.setExposedPorts(exposedPorts);
    }
}*/
