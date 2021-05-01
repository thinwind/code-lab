package org.rpis5.chapters.chapter_10.controller;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//@Slf4j
@RestController
//@RequiredArgsConstructor
public class ProxyController {
   private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
   private final MeterRegistry registry;
   private final WebClient.Builder webClientBuilder;

   public ProxyController(MeterRegistry registry, WebClient.Builder webClientBuilder) {
      this.registry = registry;
      this.webClientBuilder = webClientBuilder;
   }


   @GetMapping(path = "/proxy")
   public Mono<ResponseEntity<String>> proxyRequest(@RequestParam(name = "q") String uri) {
      String targetUri = "https://" + uri;

      return webClientBuilder
         .build()
         .get()
         .uri(targetUri)
         .exchange()
         .name("proxy.request")
         .metrics()
         .doOnNext(v -> log.info("Proxying for: {}", targetUri))
         .flatMap(cr -> cr.toEntity(String.class))
         .doOnTerminate(() -> registry
               .counter("proxy.request", "uri", targetUri)
               .increment());
   }

}
