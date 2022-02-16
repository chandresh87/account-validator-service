package com.cm.account.validator.remote;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWireMock(port = 0)
@ExtendWith(SpringExtension.class)
class AccountValidationRemoteServiceImplTest {

  @Autowired private WireMockServer wireMockServer;
  private AccountValidationRemoteService accountValidationRemoteService;

  @BeforeEach
  public void setup() {
    Map<String, WebClient> webClientMap = new HashMap<>();
    WebClient webClient = WebClient.create(wireMockServer.baseUrl() + "/api/v1/account/validate");

    webClientMap.put("source1", webClient);
    webClientMap.put("source2", webClient);
    webClientMap.put("source3", webClient);

    accountValidationRemoteService = new AccountValidationRemoteServiceImpl(webClientMap);
  }

  @Test
  void validateAccountNumber() {
    String response = "{\"isValid\": true}";
    stubFor(
        post(urlEqualTo("/api/v1/account/validate"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(response)
                    .withFixedDelay(1000)));

    AccountDTO accountDTO = new AccountDTO();
    accountDTO.setAccountNumber(12346);

    List<String> sources = new ArrayList<>();
    sources.add("source1");
    sources.add("source2");
    sources.add("source3");
    accountDTO.setSources(sources);

    Flux<ValidationDTO> validationDTOFlux =
        accountValidationRemoteService.validateAccountNumber(accountDTO);
    StepVerifier.create(validationDTOFlux)
        .consumeNextWith(
            validationDTO -> {
              assertTrue(validationDTO.isValid());
            })
        .consumeNextWith(
            validationDTO -> {
              assertTrue(validationDTO.isValid());
            })
        .consumeNextWith(
            validationDTO -> {
              assertTrue(validationDTO.isValid());
            })
        .verifyComplete();
  }
}
