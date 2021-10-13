package com.github.deergate.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
public class WebfluxDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxDemoApplication.class, args);
	}

    @Bean
    public WebClient webClient(){
        ConnectionProvider provider=ConnectionProvider.builder("test").maxConnections(20).build();
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(provider).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)))
        .baseUrl("http://127.0.0.2:8081")
        .build();
    }
}
