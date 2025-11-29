package cl.milsabores.pasteleria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class PersistenceConfig {
    // Habilita auditoría JPA para timestamps automáticos
}
