package com.github.deergate.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebfluxDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxDemoApplication.class, args);
	}

    @Bean
    public WebClient webClient(){
        return WebClient.builder().baseUrl("http://127.0.0.1:8081").build();
    }
}
