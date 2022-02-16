package com.cm.account.validator;

import com.cm.account.validator.api.ValidationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles({"test"})
class AccountValidatorApplicationTests {

  @Autowired private WebTestClient webTestClient;

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
  void accountValidatorTest() {

    String request = "{\"accountNumber\": 123456}";
    long start = System.currentTimeMillis();

    webTestClient
        .post()
        .uri("/api/v1/validate/account")
        .body(BodyInserters.fromValue(request))
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(ValidationModel.class)
        .hasSize(10);
    long end = System.currentTimeMillis();
    long totalExecutionTime = end - start;
    assertTrue(totalExecutionTime < 2000);
  }
}
