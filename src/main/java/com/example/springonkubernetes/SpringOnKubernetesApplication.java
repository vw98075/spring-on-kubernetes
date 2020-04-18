package com.example.springonkubernetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@SpringBootApplication
public class SpringOnKubernetesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOnKubernetesApplication.class, args);
	}

}

@RestController
class MyController {

	@GetMapping("/")
	public Mono<String> hello(){
		return Mono.just(new String("Hello, world. It is " + LocalDateTime.now()));
	}
}