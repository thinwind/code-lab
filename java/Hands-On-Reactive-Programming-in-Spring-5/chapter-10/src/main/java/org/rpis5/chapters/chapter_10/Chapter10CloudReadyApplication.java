package org.rpis5.chapters.chapter_10;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import io.micrometer.core.instrument.MeterRegistry;
import org.rpis5.chapters.chapter_10.acturator.ServerTimeEndpoint;
import org.rpis5.chapters.chapter_10.scheduler.MeteredSchedulersFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

//@Slf4j
//@RequiredArgsConstructor
@EnableAdminServer
@SpringBootApplication
public class Chapter10CloudReadyApplication {
	private static final Logger log = LoggerFactory.getLogger(Chapter10CloudReadyApplication.class);
	private final MeterRegistry meterRegistry;

	public Chapter10CloudReadyApplication(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter10CloudReadyApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Hooks.onNextDropped(c -> meterRegistry.counter("reactor.dropped.events").increment());
		Schedulers.setFactory(new MeteredSchedulersFactory(meterRegistry));
		log.info("Updated Scheduler factory with a custom instance");
	}
}
