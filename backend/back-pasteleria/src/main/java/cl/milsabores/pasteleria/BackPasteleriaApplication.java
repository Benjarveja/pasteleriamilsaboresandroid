package cl.milsabores.pasteleria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackPasteleriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackPasteleriaApplication.class, args);
    }

}
