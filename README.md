# Account Validator Service

# Requirement
You need to implement a rest service that accepts requests to validate an account and returns information if the requested account is valid. 

The service doesn't store any data but instead sends requests to other account data sources, aggregates this data and returns to the client.
Response is an array of objects, each object has two fields: source and isValid. Source is a string and is the name of a data source, isValid is a boolean value that data source returned.

# Solution

## Technology Stack:

     1. Java
	 2. Spring webflux
	 3. MapStruct
 	 4. Maven

## Explanation

We have used spring webflux and webclient to solve the problem. 
The important part of the requirement was to send the concurrent request to external sources, which takes 1 sec to
respond. Our service should not take more than 2 sec to respond back. 

```

private Mono<ValidationDTO> callExternalSource(
      WebClient webClient, ValidationRequestDTO validationRequestDTO, String source) {
    return webClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(validationRequestDTO))
        .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ValidationResponseDTO.class))
        .elapsed()
        .doOnNext(
            tuple ->
                log.info(
                    "Time taken by the service {} is {} with response {}",
                    source,
                    tuple.getT1(),
                    tuple.getT2().getIsValid()))
        .map(tuple -> new ValidationDTO(source, tuple.getT2().getIsValid()));
  }
  
 List<Mono<ValidationDTO>> monoList =
          webClientMap.entrySet().stream()
              .map(
                  entry -> {
                    ValidationRequestDTO validationRequestDTO =
                        new ValidationRequestDTO(accountDTO.getAccountNumber());
                    return callExternalSource(
                        entry.getValue(), validationRequestDTO, entry.getKey());
                  })
              .collect(Collectors.toList());

      validationDTOFlux = Flux.merge(monoList);
```
We have used webclient to call the external sources and merged the response using Flux.merge.
The default concurrency level for merge is 256, which is sufficient in our case else we can use other 
overloaded version of merge.

```
stubFor(
post(urlEqualTo("/api/v1/account/validate"))
.willReturn(
aResponse()
.withHeader("Content-Type", "application/json;charset=UTF-8")
.withBody(response)
.withFixedDelay(1000)
));
```
There is  test which uses wiremock. Wiremock is configured to return a response with fixed delay of 1sec.
Test is asserting that response from the controller is received under 2 sec. We are making 10 concurrent calls
to the wiremock. 

```
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

assertTrue(totalExecutionTime < 2000);

```