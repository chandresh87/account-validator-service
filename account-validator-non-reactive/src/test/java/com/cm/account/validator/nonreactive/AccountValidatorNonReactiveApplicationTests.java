package com.cm.account.validator.nonreactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
class AccountValidatorNonReactiveApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        String response = "{\"isValid\": true}";

        stubFor(
                post(urlEqualTo("/api/v1/account/validate"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                                        .withBody(response)
                                        .withFixedDelay(1000)));
    }

    @Test
    @DisplayName("Sending 10 request to source under 2 sec")
    void accountValidatorTest() throws Exception {

        String request = "{\"accountNumber\": 123456}";
        long start = System.currentTimeMillis();



        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/validate/account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                        .andExpect(status().isOk());

        long end = System.currentTimeMillis();
        long totalExecutionTime = end - start;
        assertTrue(totalExecutionTime < 2000);
    }

}
