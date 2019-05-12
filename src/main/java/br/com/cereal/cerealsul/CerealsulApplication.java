package br.com.cereal.cerealsul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CerealsulApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CerealsulApplication.class, args);
    }

}
