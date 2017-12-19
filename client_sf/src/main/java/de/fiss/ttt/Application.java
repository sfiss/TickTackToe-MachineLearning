package de.fiss.ttt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;


public class Application {
    public static void main(String... args) {
        RestTemplate restTemplate = new RestTemplate();
        GameDTO game = restTemplate.getForObject("http://localhost:8080/game/status", GameDTO.class);
        System.out.println(game);
    }
}
