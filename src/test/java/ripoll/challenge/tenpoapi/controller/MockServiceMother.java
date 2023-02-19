package ripoll.challenge.tenpoapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ripoll.challenge.tenpoapi.config.SpringConfig;
import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static ripoll.challenge.tenpoapi.controller.ControllersE2ETest.wireMock;

public class MockServiceMother {

    public static ObjectMapper objectMapper = SpringConfig.defaultObjectMapper();

    public static void integrationTaxError500() {
        String path = "/tax/payments/current";

        wireMock.stubFor(get(path)
                .willReturn(serverError()));
    }

    public static void integrationTaxSuccess(double value) throws JsonProcessingException {
        String path = "/tax/payments/current";

        wireMock.stubFor(get(path)
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(new TaxPercentage(value)))
                )
        );
    }
}
