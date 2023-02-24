package project.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
public class PersistenceConfiguration {

    @Bean
    AuditorAware<String> auditorAwareProvider (){return new AuditorAwareImpl();}
}
