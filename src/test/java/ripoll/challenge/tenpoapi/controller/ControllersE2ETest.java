package ripoll.challenge.tenpoapi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.RequestLog;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ripoll.challenge.tenpoapi.controller.PaymentController.ENDPOINT_PAYMENTS_BRIEF;
import static ripoll.challenge.tenpoapi.controller.MockServiceMother.objectMapper;
import static ripoll.challenge.tenpoapi.controller.RequestLogController.ENDPOINT_REQUEST_LOG_HISTORY;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllersE2ETest extends IntegrationTest {

    //TODO: analyze why separating Test in more class the testcontainers generates problems

    /**
     *  PaymentController Test Section
     */
    @Test
    void getTotalWithTaxes_returnsInternalServerErrorWhenNoTaxInfoExistInCacheAndTaxIntegrationFail() throws Exception {
        // Arrange
        MockServiceMother.integrationTaxError500();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/payments/brief")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstTxAmount\":80,\"secondTxAmount\":20}");

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", containsString("could not get the tax at")));
    }

    @Test
    void getTotalWithTaxes_returnsOkResponseUsingTaxValueFromCacheBecauseTaxIntegrationFail() throws Exception {
        // Arrange
        MockServiceMother.integrationTaxError500();
        memoryTaxCache.add("tax", new TaxCacheEntry(50.0d, Duration.ofHours(-1L))); //expire
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/payments/brief")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstTxAmount\":40,\"secondTxAmount\":10}");

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.first_tx_amount").value(40.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.second_tx_amount").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.total_payments").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount").value(75.0));
    }

    @Test
    void getTotalWithTaxes_returnsOkResponseUsingTaxValueFromTaxIntegrationBecauseCacheDataIsExpire() throws Exception {
        // Arrange
        MockServiceMother.integrationTaxSuccess(10.0);
        memoryTaxCache.add("tax", new TaxCacheEntry(50.0d, Duration.ofHours(-1L))); //expire
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/payments/brief")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstTxAmount\":40,\"secondTxAmount\":10}");

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.first_tx_amount").value(40.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.second_tx_amount").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.total_payments").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount").value(55.0));
    }

    @Test
    void getTotalWithTaxes_returnsOkResponseUsingTaxValueFromCache() throws Exception {
        // Arrange
        MockServiceMother.integrationTaxSuccess(10.0);
        memoryTaxCache.add("tax", new TaxCacheEntry(25d, Duration.ofMinutes(1L))); //not expire
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/accountant/payments/brief")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstTxAmount\":40,\"secondTxAmount\":10}");

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.first_tx_amount").value(40.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.second_tx_amount").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payments.total_payments").value(50.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxes").value(25.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_amount").value(62.5));
    }

    @Test
    void getTotalWithTaxes_returnsBadRequestResponseWhenInvalidRequestBody() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ENDPOINT_PAYMENTS_BRIEF)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstTxAmount\":-1,\"secondTxAmount\":33}");

        // Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    /**
     *  RequestLogController Test Section
     */
    @Test
    @Order(1)
    void getRequestLogHistory_returnsOkResponse() throws Exception {
        // Arrange
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        String method = "POST";
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].response", containsString("message")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].response", containsString("success")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].status_code").value(statusCode));
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
}
